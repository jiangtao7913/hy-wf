package com.hy.wf.admin.modules.dao.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hy-wf
 * @description: 插件表
 * @author: jt
 * @create: 2019-01-16 16:06
 **/
@TableName("t_plugin_config")
@Data
public class PluginConfig extends BaseEntity {

    private static final long serialVersionUID = -7268343430819510025L;

    private int orders;

    private int isEnabled;

    private String pluginName;

    public enum IsEnabled{
        unable(0,"不启用"),
        enable(1,"启用");

        public final  int value;
        public final String remark;

        IsEnabled(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }

}
