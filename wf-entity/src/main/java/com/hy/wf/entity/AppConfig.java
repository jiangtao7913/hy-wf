package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description: app配置信息
 * @author: jt
 * @create: 2019-01-08 16:25
 **/
@TableName("app_config")
@Data
public class AppConfig extends BaseEntity {
    private static final long serialVersionUID = 4693346549816311638L;

    private String name;
    private String value;
    private String model;
    private String remark;
    private String appType;
}
