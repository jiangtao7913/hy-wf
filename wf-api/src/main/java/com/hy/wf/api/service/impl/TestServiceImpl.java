package com.hy.wf.api.service.impl;

import com.hy.wf.api.dao.repository.Test.TestRepository;
import com.hy.wf.api.dao.repository.Test.User;
import com.hy.wf.api.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @program: hy-wf
 * @description: 测试类
 * @author: jt
 * @create: 2019-01-07 17:07
 **/
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestRepository testRepository;

    @Override
    @Cacheable(value = "user",key = "#id")
    public User test(Long id) {
        return testRepository.findById(id);
    }

    @Override
    @Cacheable(value = "1")
    public String test1(String uid) {
        System.out.println("没有走缓存");
        return "111111";
    }




}
