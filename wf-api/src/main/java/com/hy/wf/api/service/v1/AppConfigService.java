package com.hy.wf.api.service.v1;

import com.hy.wf.entity.AppConfig;

import java.util.List;

/**
 * @program: hy-wf
 * @description: app配置信息服务
 * @author: jt
 * @create: 2019-01-08 16:59
 **/

public interface AppConfigService {
    
    /** 
    * @Description: 获取配置信息
    * @Param [module, key]
    * @return: java.lang.String 
    * @Author: jt 
    * @Date: 2019/1/8 
    */ 
    String getValue(String module, String name,String appType);

    /**
    * @Description: 设置配置信息
    * @Param [module,name, value]
    * @return: boolean
    * @Author: jt
    * @Date: 2019/1/8
    */
    boolean setValue(String module, String name, String value,String appType);


    List<AppConfig> findByModelAndAppType(String model,String appType);
}
