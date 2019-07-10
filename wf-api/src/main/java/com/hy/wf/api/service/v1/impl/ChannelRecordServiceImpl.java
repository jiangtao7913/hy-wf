package com.hy.wf.api.service.v1.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.hy.wf.api.dao.repository.v1.ChannelRecordRepository;
import com.hy.wf.api.dao.repository.v1.InstallLogRepository;
import com.hy.wf.api.service.v1.ChannelRecordService;
import com.hy.wf.entity.ChannelRecord;
import com.hy.wf.entity.InstallLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:40
 **/
@Service
public class ChannelRecordServiceImpl implements ChannelRecordService {

    @Autowired
    private ChannelRecordRepository channelRecordRepository;
    @Autowired
    private InstallLogRepository installLogRepository;


    @Override
    public ChannelRecord findByDate(String date,String source,String appType) {
        return channelRecordRepository.findByDate(date,source,appType);
    }

    @Override
    public void recordChannel(String date, String appType, String source,String hardwareKey,int install,InstallLog installLog) {
        ChannelRecord channelRecord = findByDate(date,source,appType);
        if(null == channelRecord){
            channelRecord = new ChannelRecord();
            channelRecord.setDate(date);
            channelRecord.setTotalIncome(BigDecimal.ZERO);
            channelRecord.setChannelName(source);
            channelRecord.setChannelInstallTotal(install);
            channelRecord.setChannelDeductTotal(BigDecimal.ZERO);
            channelRecord.setAppType(appType);
            channelRecord.init();
            channelRecordRepository.save(channelRecord);
        }else {
            //统计安装人数
            if(null == installLog){
                channelRecord.setChannelInstallTotal(channelRecord.getChannelInstallTotal()+1);
                channelRecordRepository.updateColumnById("channel_install_total",String.valueOf(channelRecord.getChannelInstallTotal()),channelRecord.getId());
            }

            //InstallLog installLog1 = installLogRepository.findByHardwareKeyAndModifyDate(hardwareKey,date);
//            if(null == installLog1){
////                channelRecord.setChannelInstallTotal(channelRecord.getChannelInstallTotal()+1);
////                channelRecordRepository.updateColumnById("channel_install_total",String.valueOf(channelRecord.getChannelInstallTotal()),channelRecord.getId());
////            }
        }
    }

    @Override
    public void updateChannelRecord(String date, String appType, String source, BigDecimal totalIncome, BigDecimal channelDeductTotal) {
        ChannelRecord channelRecord = findByDate(date,source,appType);
        channelRecord.setTotalIncome(channelRecord.getTotalIncome().add(totalIncome));
        channelRecord.setChannelDeductTotal(channelRecord.getChannelDeductTotal().add(channelDeductTotal));
        channelRecordRepository.updateById(channelRecord);
    }

}
