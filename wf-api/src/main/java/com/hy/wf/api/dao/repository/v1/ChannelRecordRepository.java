package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.ChannelRecord;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:32
 **/
public interface ChannelRecordRepository extends BaseRepository<ChannelRecord,Long> {

    ChannelRecord findByDate(String date,String source,String appType);

    int updateColumnById(String column,String columnValue,Long id);

    int updateById(ChannelRecord channelRecord);
}
