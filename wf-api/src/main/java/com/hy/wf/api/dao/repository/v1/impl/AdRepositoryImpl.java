package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.AdRepository;
import com.hy.wf.entity.Ad;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @program: hy-wf
 * @description: 广告
 * @author: jt
 * @create: 2019-01-09 10:59
 **/
@Repository
public class AdRepositoryImpl extends BaseRepositoryImpl<Ad,Long> implements AdRepository {

    private static final String TABLE_NAME = "t_ad";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Ad> getBeanClass() {
        return Ad.class;
    }

    @Override
    public List<Ad> findByPositionBetweenStartDateAndEndDateAndAppType(String position, Date startDate, Date endDate,String appType) {
        return select().where(Condition.equal("position",position)).where(Condition.equal("app_type",appType))
                .date("begin_date","end_date",startDate,endDate).
                comm().find();
    }
}
