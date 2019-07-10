package com.hy.wf.entity;

import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 16:33
 **/
@Data
public class Sn extends BaseEntity {
    private static final long serialVersionUID = -8289306257356997052L;

    private Long lastValue;

    private int type;

    /**
     * 类型
     */
    public enum Type {

        uid(0,"UID"),

        /** 订单 */
        order(1,"订单"),
        money(2,"交易")
        ;
        public final  int value;
        public final String remark;

        Type(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }
}

