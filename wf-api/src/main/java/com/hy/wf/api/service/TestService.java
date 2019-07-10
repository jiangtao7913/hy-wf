package com.hy.wf.api.service;

import com.hy.wf.api.dao.repository.Test.User;

/**
 * @program: hy-wf
 * @description: 测试service
 * @author: jt
 * @create: 2019-01-07 17:06
 **/
public interface TestService {

    User test(Long id);

    String test1(String uid);

    //String test2();
}
