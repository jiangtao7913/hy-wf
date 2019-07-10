package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:17
 **/
@TableName("t_vip")
@Data
public class Vip extends BaseEntity {
    private static final long serialVersionUID = 4782230585779193008L;

    private int type;
    private BigDecimal price;
    private String meno;

    private String title;

    private String appType;

    public enum Type{
        Month(0,"月会员"),
        Year(1,"年会员")
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
