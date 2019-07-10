package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.ChannelRecordRepository;
import com.hy.wf.entity.ChannelRecord;
import org.springframework.stereotype.Repository;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:35
 **/
@Repository
public class ChannelRecordRepositoryImpl extends BaseRepositoryImpl<ChannelRecord,Long> implements ChannelRecordRepository {
    private static final String TABLE_NAME = "t_channel_record";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<ChannelRecord> getBeanClass() {
        return ChannelRecord.class;
    }

    @Override
    public ChannelRecord findByDate(String date,String source,String appType) {
        return select().where(Condition.equal("date",date)).where(Condition.equal("channel_name",source))
                .where(Condition.equal("app_type",appType)).findOne();
    }

    @Override
    public int updateColumnById(String column, String columnValue, Long id) {
        return update().set(column,columnValue).where(Condition.equal("id",id)).update();
    }

    @Override
    public int updateById(ChannelRecord channelRecord) {
        return update().set("total_income",channelRecord.getTotalIncome()).set("channel_deduct_total",channelRecord.getChannelDeductTotal())
                .where(Condition.equal("id",channelRecord.getId())).update();
    }
}
