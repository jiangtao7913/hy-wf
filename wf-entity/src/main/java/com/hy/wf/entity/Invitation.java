package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-12 16:31
 **/
@TableName("t_invitation")
@Data
public class Invitation extends BaseEntity {
    private static final long serialVersionUID = 4981989189644045181L;

    private Long masterId;

    private String masterUid;

    private BigDecimal incomeTotal;

    private BigDecimal stairIncome;

    private BigDecimal secondIncome;

    private int count;

}
