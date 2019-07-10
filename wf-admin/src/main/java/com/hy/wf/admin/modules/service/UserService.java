package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.admin.modules.sys.entity.SysUserEntity;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.User;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 15:58
 **/
public interface UserService extends IService<User> {

    PageUtils queryPage(Map<String, Object> params);

    void save(User user);

    /**
     * 修改用户
     */
    void update(User user);

    void deleteIdentify(Long[] userIds);
}
