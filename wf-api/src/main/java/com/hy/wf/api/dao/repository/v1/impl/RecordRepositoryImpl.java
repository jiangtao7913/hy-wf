package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.RecordRepository;
import com.hy.wf.entity.Record;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:33
 **/
@Repository
public class RecordRepositoryImpl extends BaseRepositoryImpl<Record,Long> implements RecordRepository {
    private static final String TABLE_NAME = "t_record";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Record> getBeanClass() {
        return Record.class;
    }

    @Override
    public Record findByDate(String date) {
        return select().where(Condition.equal("date",date)).findOne();
    }

    @Override
    public int updateColumnById(String column, String columnValue, Long id) {
        return update().set(column,columnValue).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateById(Record record) {
        return update().set("total_income",record.getTotalIncome()).set("wx_income",record.getWxIncome())
                .set("zfb_income",record.getZfbIncome()).set("balance_income",record.getBalanceIncome())
                .set("total_deduct",record.getTotalDeduct()).where(Condition.equal("id",record.getId())).update();
    }
}
