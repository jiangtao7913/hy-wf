package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Invitation;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 18:07
 **/
public interface InvitationService  extends IService<Invitation> {

    void save(Invitation invitation);

    void update(Invitation invitation);

    PageUtils queryPage(Map<String, Object> params);

    void delete(Long[] ids);

}
