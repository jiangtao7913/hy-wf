package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:15
 **/
@TableName("t_record")
@Data
public class Record extends BaseEntity {

    private String date;
    private BigDecimal totalIncome;
    private BigDecimal wxIncome;
    private BigDecimal zfbIncome;
    private BigDecimal balanceIncome;
    private BigDecimal totalDeduct;
    private int totalInstall;
    private int dayActiveCount;
}
