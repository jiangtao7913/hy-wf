package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description: app安装记录表
 * @author: jt
 * @create: 2019-01-08 11:25
 **/
@TableName("t_install_log")
@Data
public class InstallLog extends BaseEntity {

    private static final long serialVersionUID = -6971643776418438915L;

    private String hardwareKey;

    private String ip;

    private String clientType;

    private String clientVersion;

    private String memo;

    private String source;

    private String appType;

//    private int freeTime;
//
//    private int freeCount;

}
