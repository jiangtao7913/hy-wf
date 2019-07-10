package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.base.BaseEntity;

import java.util.Date;


public interface InstallLogRepository extends BaseRepository<InstallLog,Long> {
    
    /** 
    * @Description:  查询安装记录
    * @Param [hardwareKey, dataStatus]
    * @return: com.hy.wf.dao.entity.v1.InstallLog
    * @Author: jt 
    * @Date: 2019/1/8 
    */ 
    InstallLog findByHardwareKeyAndDataStatus(String hardwareKey, BaseEntity.DataStatus dataStatus);

    int updateById(Long id, String hardwareKey, String deviceModel, String ip, String version, Date date);

    /** 
    * @Description: 修改免费次数
    * @Param [hardwareKey, freeTime]
    * @return: int 
    * @Author: jt 
    * @Date: 2019/2/26 
    */ 
    int updateFreeById(Long id,int freeTime);

    InstallLog findByHardwareKeyAndModifyDate(String hardwareKey,String date);
}
