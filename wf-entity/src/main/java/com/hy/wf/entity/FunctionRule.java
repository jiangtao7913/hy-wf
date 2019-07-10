package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:36
 **/
@TableName("t_function_rule")
@Data
public class FunctionRule extends BaseEntity {

    private static final long serialVersionUID = -5974291043022211387L;

    private Long functionId;

    private int type;

    private String meno;

    private String checkValue;

    public enum Type{
        TY(0,"通用功能"),
        ZF(1,"转发功能"),
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
