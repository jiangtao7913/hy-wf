package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @program: hy-wf
 * @description: 广告实体
 * @author: jt
 * @create: 2019-01-09 10:50
 **/
@TableName("t_ad")
@Data
public class Ad extends BaseEntity {

    private static final long serialVersionUID = 1664867112542489931L;

    private int orders;
    private String title;
    private int type;
    private String content;
    private String path;
    private Date beginDate;
    private Date endDate;
    private String url;
    private String position;
    private String appType;

    public enum Type{
        One(0,"banner"),

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
