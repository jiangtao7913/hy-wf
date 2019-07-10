package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.UserFunctionRepository;
import com.hy.wf.entity.UserFunction;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-04 18:57
 **/
@Repository
public class UserFunctionRepositoryImpl extends BaseRepositoryImpl<UserFunction,Long> implements UserFunctionRepository {
    private static final String TABLE_NAME = "t_user_function";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<UserFunction> getBeanClass() {
        return UserFunction.class;
    }

    @Override
    public List<UserFunction> findByUserId(Long userId) {
        return select().where(Condition.equal("user_id",userId)).comm().orderBy("type",false).find();
    }

    @Override
    public UserFunction findByUserIdAndTypeAndId(Long functionId,Long userId, UserFunction.Type type) {
        return select().where(Condition.equal("function_id",functionId)).where(Condition.equal("user_id",userId)).where(Condition.equal("type",type.value)).comm().findOne();
    }

    @Override
    public int updateExpireTimeById(Long id, Date expireTime) {
        return update().set("expire_time",expireTime).where(Condition.equal("id",id)).update();
    }


    @Override
    public List<UserFunction> findByUserIdAndType(Long userId, UserFunction.Type type) {
        return select().where(Condition.equal("user_id",userId)).where(Condition.equal("type",type.value)).comm().find();
    }

    @Override
    public UserFunction findByUserIdAndFunctionId(Long functionId, Long userId) {
        return select().where(Condition.equal("user_id",userId)).where(Condition.equal("function_Id",functionId)).comm().findOne();
    }

    @Override
    public UserFunction findByUserIdAndVip(Long userId, UserFunction.Type type) {
        return select().where(Condition.equal("user_id",userId)).where(Condition.equal("type",type.value)).comm().findOne();

    }


}
