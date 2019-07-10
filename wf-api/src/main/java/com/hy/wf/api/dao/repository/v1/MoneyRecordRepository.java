package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.MoneyRecord;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-13 16:23
 **/
public interface MoneyRecordRepository extends BaseRepository<MoneyRecord,Long> {

    List<MoneyRecord> findByUserIdAndType(Long userId, Integer offset, Integer limit, MoneyRecord.Type type);

    List<MoneyRecord> findByUserIdAndNotType(Long userId, Integer offset, Integer limit, MoneyRecord.Type type);
}
