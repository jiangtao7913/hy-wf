package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @program: hy-wf
 * @description: 用户功能表
 * @author: jt
 * @create: 2019-03-04 18:52
 **/
@TableName("t_user_function")
@Data
public class UserFunction extends BaseEntity {

    private static final long serialVersionUID = -6610613799871123778L;

    private Long userId;

    private Long functionId;

    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    private int type;

    private String functionName;

    public enum Type{
        Normal(0,"一般功能"),
        Vip(1,"vip功能"),
        Extra(2,"扩展功能"),
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
