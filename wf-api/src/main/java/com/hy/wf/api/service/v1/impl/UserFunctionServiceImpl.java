package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.UserFunctionRepository;
import com.hy.wf.api.service.v1.UserFunctionService;
import com.hy.wf.entity.UserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-06 11:27
 **/
@Service
public class UserFunctionServiceImpl implements UserFunctionService {

    @Autowired
    private UserFunctionRepository userFunctionRepository;

    @Override
    public List<UserFunction> findByUserId(Long userId) {
        return userFunctionRepository.findByUserId(userId);
    }
}
