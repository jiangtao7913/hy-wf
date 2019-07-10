package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.InvitationDao;
import com.hy.wf.admin.modules.service.InvitationService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.Invitation;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 18:08
 **/
@Service("invitationService")
public class InvitationServiceImpl extends ServiceImpl<InvitationDao, Invitation> implements InvitationService {

    @Override
    public void save(Invitation invitation) {
        invitation.init();
        this.insert(invitation);
    }

    @Override
    public void update(Invitation invitation) {
        this.update(invitation, new EntityWrapper<Invitation>().eq("master_id", invitation.getMasterId()));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String masterId = (String)params.get("masterId");

        Page<Invitation> page = this.selectPage(
                new Query<Invitation>(params).getPage(),
                new EntityWrapper<Invitation>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(masterId),"master_id", masterId)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void delete(Long[] ids) {
        this.deleteBatchIds(Arrays.asList(ids));
    }

}
