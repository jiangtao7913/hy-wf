package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Record;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:31
 **/
public interface RecordRepository extends BaseRepository<Record,Long> {

    /** 
    * @Description: 根据日期查找统计记录
    * @Param [date]
    * @return: com.hy.wf.entity.Record 
    * @Author: jt 
    * @Date: 2019/3/26 
    */ 
    Record findByDate(String date);

    int updateColumnById(String column,String columnValue,Long id);

    int updateById(Record record);
}
