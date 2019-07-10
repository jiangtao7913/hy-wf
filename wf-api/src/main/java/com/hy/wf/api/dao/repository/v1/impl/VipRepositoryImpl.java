package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.VipRepository;
import com.hy.wf.entity.Vip;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:20
 **/
@Repository
public class VipRepositoryImpl extends BaseRepositoryImpl<Vip,Long> implements VipRepository {
    private static final String TABLE_NAME = "t_vip";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Vip> getBeanClass() {
        return Vip.class;
    }

    @Override
    public List<Vip> findList(String appType) {
        return select().where(Condition.equal("app_type",appType)).comm().orderBy("type",false).find();
    }
}
