package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.AppConfig;

import java.util.List;

/**
 * @program: hy-wf
 * @description: app配置信息
 * @author: jt
 * @create: 2019-01-08 16:36
 **/
public interface AppConfigRepository extends BaseRepository<AppConfig,Long> {
    
    /** 
    * @Description: 通过module查询
    * @Param [module]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.AppConfig>
    * @Author: jt 
    * @Date: 2019/1/8 
    */ 
    List<AppConfig> findByModelAndAppType(String model,String appType);

    /**
    * @Description: 根据module 和name查询
    * @Param [module, name]
    * @return: com.hy.wf.dao.entity.v1.AppConfig
    * @Author: jt
    * @Date: 2019/1/8
    */
    AppConfig findByModelAndNameAndAppType(String model, String name,String appType);

    /**
    * @Description: 修改value值根据id
    * @Param [id, value]
    * @return: int
    * @Author: jt
    * @Date: 2019/1/8
    */
    int updateValueById(Long id, String value);

}
