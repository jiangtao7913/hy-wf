package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.base.SelectBuilder;
import com.hy.wf.api.dao.repository.v1.MoneyRecordRepository;
import com.hy.wf.entity.MoneyRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-13 16:23
 **/
@Repository
public class MoneyRecordRepositoryImpl extends BaseRepositoryImpl<MoneyRecord,Long> implements MoneyRecordRepository {

    private static final String TABLE_NAME = "t_money_record";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<MoneyRecord> getBeanClass() {
        return MoneyRecord.class;
    }

    @Override
    public List<MoneyRecord> findByUserIdAndType(Long userId, Integer offset, Integer limit, MoneyRecord.Type type) {
        SelectBuilder<MoneyRecord, Long> selectBuilder = select().where(Condition.equal("user_id",userId)).where(Condition.equal("type",type.value));
        if( 0 != offset){
            selectBuilder.where(Condition.lessThen("id",offset));
        }
        return selectBuilder.comm().limit(limit).orderBy("id",false).find();
    }

    @Override
    public List<MoneyRecord> findByUserIdAndNotType(Long userId, Integer offset, Integer limit, MoneyRecord.Type type) {
        SelectBuilder<MoneyRecord, Long> selectBuilder = select().where(Condition.equal("user_id",userId)).where(Condition.notIn("type",new Integer[]{type.value}));
        if( 0 != offset){
            selectBuilder .where(Condition.lessThen("id",offset));
        }
        return selectBuilder.comm().limit(limit).orderBy("id",false).find();
    }
}
