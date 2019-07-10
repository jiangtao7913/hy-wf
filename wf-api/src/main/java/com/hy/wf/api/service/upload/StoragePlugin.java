/*
 * Copyright 2017-2018 All rights reserved.
 * Support: JinXing
 * License: 
 */
package com.hy.wf.api.service.upload;

import java.io.File;
import java.io.InputStream;

import com.hy.wf.api.service.v1.PluginConfigService;
import com.hy.wf.entity.PluginConfig;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Plugin - 存储
 *
 * @author JinXing Team
 * @version 1.0
 * @email 
 * @date 2018-04-20 00:12:23
 * @version 1.0
 */
public abstract class StoragePlugin implements Comparable<StoragePlugin> {

	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	public final String getPluginName() {
		return getClass().getAnnotation(Component.class).value();
	}

	/**
	 * 获取插件配置
	 *
	 * @return 插件配置
	 */
	public PluginConfig getPluginConfig(String appType) {
		return pluginConfigService.findByPluginName(getPluginName(),appType) ;
	}

	/**
	 * 获取是否已启用
	 * 
	 * @return 是否已启用
	 */
	public boolean getIsEnabled(String appType) {
		PluginConfig pluginConfig = getPluginConfig(appType);
        if(pluginConfig == null || pluginConfig.getIsEnabled() == PluginConfig.IsEnabled.unable.value){
            return false;
        }
        return true;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	public String getAttribute(String name,String appType) {
		PluginConfig pluginConfig = getPluginConfig(appType);
		return pluginConfig != null ? pluginConfig.getAttribute(name) : null;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getOrder(String appType) {
		PluginConfig pluginConfig = getPluginConfig(appType);
		return pluginConfig != null ? pluginConfig.getOrders() : null;
	}

	/**
	 * 文件上传
	 * 
	 * @param path 上传路径
	 * @param inputStream 上传文件
	 * @param contentType 文件类型
	 */
	public abstract boolean upload(String path, InputStream inputStream, String contentType,String appType);
	
	/**
	 * 删除文件
	 *
	 * @param fileName
	 */
	public abstract void delete(String fileName,String appType);

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		StoragePlugin other = (StoragePlugin) obj;
		return new EqualsBuilder().append(getPluginName(), other.getPluginName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getPluginName()).toHashCode();
	}

	@Override
	public int compareTo(StoragePlugin storagePlugin) {
		return 0;
	}

}