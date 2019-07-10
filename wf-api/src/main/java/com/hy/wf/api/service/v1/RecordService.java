package com.hy.wf.api.service.v1;

import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.Record;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:30
 **/
public interface RecordService {

    Record findByDate(String date);

    void record(String date, String hardwareKey, int install, InstallLog installLog);

    void updateRecord(String date, BigDecimal totalIncome, BigDecimal wxIncome, BigDecimal zfbIncome, BigDecimal balanceIncome
            , BigDecimal totalDeduct);
}
