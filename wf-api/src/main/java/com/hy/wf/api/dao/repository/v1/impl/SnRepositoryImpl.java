package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.SnRepository;
import com.hy.wf.entity.Sn;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 16:39
 **/
@Repository
public class SnRepositoryImpl extends BaseRepositoryImpl<Sn,Long> implements SnRepository {
    private static final String TABLE_NAME = "t_sn";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Sn> getBeanClass() {
        return Sn.class;
    }

    @Override
    public Sn findByType(Sn.Type type) {
        return select().where(Condition.equal("type",type.value)).comm().findOne();
    }

    @Override
    public int updateById(Sn sn) {
        return update().set("last_value",sn.getLastValue()).set("modify_date",sn.getModifyDate()).where(Condition.equal("id",sn.getId())).update();
    }
}
