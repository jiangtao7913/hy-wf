package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.FunctionRuleRepository;
import com.hy.wf.entity.FunctionRule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:43
 **/
@Repository
public class FunctionRuleRepositoryImpl extends BaseRepositoryImpl<FunctionRule,Long> implements FunctionRuleRepository {
    private static final String TABLE_NAME = "t_function_rule";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<FunctionRule> getBeanClass() {
        return FunctionRule.class;
    }

    @Override
    public List<FunctionRule> findInFunctionIds(List<Long> functionIds) {
        return select().where(Condition.in("function_id",functionIds)).comm().find();
    }
}
