package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: hy-wf
 * @description: 系统日志表
 * @author: jt
 * @create: 2019-01-09 16:49
 **/
@TableName("app_log")
@Data
public class AppLog implements Serializable {
    private static final long serialVersionUID = -8473669668629637151L;
    private Long id;
    private String clientIp;
    private String uri;
    private String requireMethod;
    private String paramData;
    private Date requireTime;
    private Date endTime;
    private Long responseTime;
    private String response;
    private String stackMessage;
    private int type;
    private String uid;

    public enum Type{
        normal(0,"正常日志"),
        error(1,"错误日志");

        public final int value;
        public final String remark;

        Type(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }

    }

}
