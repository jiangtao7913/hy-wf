package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-13 16:17
 **/
@TableName("t_money_record")
@Data
public class MoneyRecord extends BaseEntity {
    private static final long serialVersionUID = -2380830283065547765L;

    private String account;

    private String name;

    private int status;

    private Long userId;

    private String meno;

    private int type;

    private String number;

    private String reason;

    private String otherUserName;

    private Long otherUserId;

    private BigDecimal amount;

    public enum Status{
        Audit(0,"审核中"),
        Successful(1,"成功"),
        Cancel(2,"取消"),

        ;
        public final  int value;
        public final String remark;

        Status(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
        public static Status valueOf(int value) {
            for (Status status : values()) {
                if (status.value == value) {
                    return status;
                }
            }
            return null;
        }
    }

    public enum Type{
        Consume(0,"消费"),
        Withdraw(1,"提现"),
        Income(2,"收入"),

        ;
        public final  int value;
        public final String remark;

        Type(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
        public static Type valueOf(int value) {
            for (Type type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }
}
