package com.hy.wf.api.service.v1;

import com.hy.wf.common.Result;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.InstallLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description: app公用service
 * @author: jt
 * @create: 2019-01-08 10:09
 **/

public interface CommonService {

    /** 
    * @Description:  
    * @Param [hardwareKey, deviceModel, ip, version, source]
    * @return: boolean 
    * @Author: jt 
    * @Date: 2019/1/8 
    */ 
    InstallLog install(String hardwareKey, String deviceModel, String ip, String version, String source,String appType);

//    /**
//    * @Description: 修改app免费试用次数
//    * @Param [hardwareKey]
//    * @return: boolean
//    * @Author: jt
//    * @Date: 2019/2/26
//    */
//    int updateInstall(String hardwareKey);

    /**
    * @Description: 根据模块获取app配置信息
    * @Param [module, version, type]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.AppConfig>
    * @Author: jt
    * @Date: 2019/1/8
    */
    Map<String,Object> version(String module, String version, String type,String appType);


    /**
    * @Description: 根据位置查询有效时间内的广告
    * @Param [position, startDate, endDate]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.Ad>
    * @Author: jt
    * @Date: 2019/1/9
    */
    List<Ad> getByPositionAndAppType(String position, Date startDate, Date endDate,String appType);


    /**
     * 查询app配置信息
     * @param module
     * @param appType
     * @return
     */
    Map<String,String>  getAppConfig(String module,String appType);

}
