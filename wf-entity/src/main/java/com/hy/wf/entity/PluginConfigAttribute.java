package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:49
 **/
@TableName("t_plugin_config_attribute")
@Data
public class PluginConfigAttribute extends BaseEntity {

    private static final long serialVersionUID = 2482998567527756226L;
    private Long pluginConfigId;
    private String name;
    private String value;
}
