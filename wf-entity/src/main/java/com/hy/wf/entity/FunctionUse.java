package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:39
 **/
@TableName("t_function_use")
@Data
public class FunctionUse extends BaseEntity {
    private static final long serialVersionUID = -1909376764507848564L;

    private Long functionId;

    private String hardwareKey;

    private int useCount;

    private Long userId;

    private int type;

    public enum Type{
        TY(0,"免费使用功能"),
        ZF(1,"付费使用功能"),
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
