package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.FunctionRepository;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.UserFunction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-02 15:24
 **/
@Repository
public class FunctionRepositoryImpl extends BaseRepositoryImpl<Function,Long> implements FunctionRepository {
    private static final String TABLE_NAME = "t_function";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Function> getBeanClass() {
        return Function.class;
    }

    /**
     * 根据位置查询功能
     * @param position
     * @return
     */
    @Override
    public List<Function> findByPositionAndAppType(String position,String appType) {
        return select().where(Condition.equal("position",position)).where(Condition.equal("app_type",appType)).comm().orderBy("status",true).orderBy("orders",true).find();
    }

    @Override
    public List<Function> findByType(Function.Type type,String appType) {
        return select().where(Condition.equal("type",type.value)).where(Condition.equal("app_type",appType)).comm().find();
    }

    @Override
    public List<Function> findByNotInIdsAndType(List<Long> ids,List<Integer> types,String appType) {
        if(ids.isEmpty()){
            return select().where(Condition.in("type",types.toArray())).where(Condition.equal("app_type",appType)).comm().find();
        }
        return select().where(Condition.notIn("id", ids.toArray())).where(Condition.in("type",types.toArray())).where(Condition.equal("app_type",appType)).comm().find();
    }
}
