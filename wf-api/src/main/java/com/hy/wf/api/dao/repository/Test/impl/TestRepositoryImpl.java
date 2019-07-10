package com.hy.wf.api.dao.repository.Test.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.repository.Test.TestRepository;
import com.hy.wf.api.dao.repository.Test.User;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-05 14:28
 **/
@Repository
public class TestRepositoryImpl extends BaseRepositoryImpl<User,Long> implements TestRepository {

    private static final String TABLE_NAME = "t_user";
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<User> getBeanClass() {
        return User.class;
    }

    @Override
    public User test(CharSequence sql, Object... args){
       return findOne(sql,args);
    }

}
