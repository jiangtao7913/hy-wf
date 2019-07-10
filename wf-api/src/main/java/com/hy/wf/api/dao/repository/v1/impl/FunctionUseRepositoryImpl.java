package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.FunctionUseRepository;
import com.hy.wf.entity.FunctionUse;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:45
 **/
@Repository
public class FunctionUseRepositoryImpl extends BaseRepositoryImpl<FunctionUse,Long> implements FunctionUseRepository {
    private static final String TABLE_NAME = "t_function_use";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<FunctionUse> getBeanClass() {
        return FunctionUse.class;
    }

    @Override
    public FunctionUse findByHardwareKeyAndType(String hardwareKey, FunctionUse.Type type) {
        return select().where(Condition.equal("hardware_key",hardwareKey)).where(Condition.equal("type",type.value)).comm().findOne();
    }

    @Override
    public int updateUserCountById(Long id, int userCount) {
        return update().set("use_count",userCount).where(Condition.equal("id",id)).update();
    }
}
