package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 18:19
 **/
@TableName("t_channel_record")
@Data
public class ChannelRecord extends BaseEntity {

    private String date;
    private BigDecimal totalIncome;
    private String channelName;
    private int channelInstallTotal;
    private BigDecimal channelDeductTotal;
    private String appType;
}
