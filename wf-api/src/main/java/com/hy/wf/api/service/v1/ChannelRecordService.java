package com.hy.wf.api.service.v1;

import com.hy.wf.entity.ChannelRecord;
import com.hy.wf.entity.InstallLog;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:39
 **/
public interface ChannelRecordService {

    ChannelRecord findByDate(String date,String source,String appType);

    void recordChannel(String date, String appType, String source, String hardwareKey, int install, InstallLog installLog);

    void updateChannelRecord(String date,String appType,String source, BigDecimal totalIncome,BigDecimal channelDeductTotal);

}
