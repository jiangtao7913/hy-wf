package com.hy.wf.admin.modules.service.impl;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.UserDao;
import com.hy.wf.admin.modules.service.InvitationService;
import com.hy.wf.admin.modules.service.UserService;
import com.hy.wf.admin.modules.sys.entity.SysUserEntity;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Invitation;
import com.hy.wf.entity.User;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 15:59
 **/
@Service("userServiceI")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private InvitationService invitationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");
        String id = (String)params.get("id");
        String uid = (String)params.get("uid");
        String hardwareKey = (String)params.get("hardwareKey");
        String sign = (String)params.get("sign");

        Page<User> page = this.selectPage(
                new Query<User>(params).getPage(),
                new EntityWrapper<User>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(name),"name", name)
                        .eq(StringUtils.isNotBlank(id),"id", id)
                        .eq(StringUtils.isNotBlank(uid),"uid", uid)
                        .like(StringUtils.isNotBlank(hardwareKey),"hardware_key", hardwareKey)
                        .like(StringUtils.isNotBlank(sign),"sign", sign)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()),Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
       return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        user.setUid("1111");
        user.init();
        this.insert(user);
        Invitation invitation = new Invitation();
        invitation.setMasterId(user.getId());
        invitation.setMasterUid(user.getUid());
        invitation.setIncomeTotal(user.getBalance());
        invitation.setStairIncome(new BigDecimal("0"));
        invitation.setSecondIncome(new BigDecimal("0"));
        invitation.setCount(0);
        invitation.init();
        invitationService.save(invitation);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        this.updateById(user);
        Invitation invitation = invitationService.selectOne(new EntityWrapper<Invitation>().eq("master_id",user.getId()));
        if(null != invitation){
            invitation.setIncomeTotal(user.getBalance());
            invitationService.update(invitation);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteIdentify(Long[] userIds) {
        this.deleteBatchIds(Arrays.asList(userIds));
        List<Invitation> invitationList = invitationService.selectList(new EntityWrapper<Invitation>().in("master_id",userIds));
        if(null != invitationList){
            List<Long> ids = invitationList.stream().map(Invitation::getId).collect(Collectors.toList());
            invitationService.deleteBatchIds(ids);
        }
    }

}
