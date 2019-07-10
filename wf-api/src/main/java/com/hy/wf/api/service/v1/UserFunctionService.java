package com.hy.wf.api.service.v1;

import com.hy.wf.entity.UserFunction;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-06 11:25
 **/
public interface UserFunctionService {

    List<UserFunction> findByUserId(Long userId);
}
