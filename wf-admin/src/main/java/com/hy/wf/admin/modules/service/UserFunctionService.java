package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.UserFunction;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 17:55
 **/
public interface UserFunctionService extends IService<UserFunction> {

    PageUtils queryPage(Map<String, Object> params);

    void save(UserFunction userFunction);
}
