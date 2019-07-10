package com.hy.wf.entity;

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
@Data
public class PluginConfig extends BaseEntity {

    private static final long serialVersionUID = -7268343430819510025L;

    private int orders;

    private int isEnabled;

    private String pluginName;

    /** 属性 */
    private Map<String, String> attributes = new HashMap<>();

    /**
     * 获取属性
     *
     * @param name 属性名称
     *
     * @return String
     */
    public String getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * 设置属性
     *
     * @param name 属性名称
     * @param value 属性值
     */
    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

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
