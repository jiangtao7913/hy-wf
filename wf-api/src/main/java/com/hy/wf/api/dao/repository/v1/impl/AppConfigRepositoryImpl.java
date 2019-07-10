package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.AppConfigRepository;
import com.hy.wf.entity.AppConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: hy-wf
 * @description: app配置信息
 * @author: jt
 * @create: 2019-01-08 16:37
 **/
@Repository
public class AppConfigRepositoryImpl extends BaseRepositoryImpl<AppConfig,Long> implements AppConfigRepository {

    private static final String TABLE_NAME = "app_config";
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<AppConfig> getBeanClass() {
        return AppConfig.class;
    }

    /** 
    * @Description: 通过module查询
    * @Param [module]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.AppConfig>
    * @Author: jt
    * @Date: 2019/1/8
    */
    @Override
    public List<AppConfig> findByModelAndAppType(String model,String appType) {
        return select().where(Condition.equal("model",model)).where(Condition.equal("app_type",appType)).comm().find();
    }

    @Override
    public AppConfig findByModelAndNameAndAppType(String model, String name,String appType) {
        return select().where(Condition.equal("name",name)).where(Condition.equal("model",model))
                .where(Condition.equal("app_type",appType))
                .comm().findOne();
    }

    @Override
    public int updateValueById(Long id, String value) {
        return update().set("value",value).where(Condition.equal("id",id)).update();
    }

}
