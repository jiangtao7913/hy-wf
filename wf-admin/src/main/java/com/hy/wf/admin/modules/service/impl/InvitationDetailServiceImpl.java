package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.InvitationDetailDao;
import com.hy.wf.admin.modules.service.InvitationDetailService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.InvitationDetail;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 15:32
 **/
@Service
public class InvitationDetailServiceImpl extends ServiceImpl<InvitationDetailDao, InvitationDetail> implements InvitationDetailService {

    @Override
    public void save(InvitationDetail invitationDetail) {
        invitationDetail.init();
        this.insert(invitationDetail);
    }

    @Override
    public void update(InvitationDetail invitationDetail) {
        this.updateById(invitationDetail);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String invitationId = (String)params.get("invitationId");

        Page<InvitationDetail> page = this.selectPage(
                new Query<InvitationDetail>(params).getPage(),
                new EntityWrapper<InvitationDetail>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(invitationId),"invitation_id", invitationId)
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
