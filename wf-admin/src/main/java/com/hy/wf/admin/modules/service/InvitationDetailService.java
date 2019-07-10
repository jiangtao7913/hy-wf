package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Invitation;
import com.hy.wf.entity.InvitationDetail;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 18:07
 **/
public interface InvitationDetailService extends IService<InvitationDetail> {

    void save(InvitationDetail invitationDetail);

    void update(InvitationDetail invitationDetail);

    PageUtils queryPage(Map<String, Object> params);

    void delete(Long[] ids);


}
