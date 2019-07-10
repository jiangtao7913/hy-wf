package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-02 15:20
 **/
@TableName("t_function")
@Data
public class Function extends BaseEntity {

    private int orders;

    private String title;

    private int type;

    private String position;

    private String url;

    private BigDecimal price;

    private String meno;

    private String icon;

    private int status;

    private String appType;

    private int location;

    private String target;

    public enum Location{
        One(0,"上面"),
        Two(1,"下面"),

        ;
        public final  int value;
        public final String remark;

        Location(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
        public static Location valueOf(int value) {
            for (Location type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Status{
        Unable(0,"未启用"),
        Able(1,"已启用"),


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
        Gereral(0,"一般功能"),
        Extend(1,"扩展功能"),
        Title(2,"标签"),

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
