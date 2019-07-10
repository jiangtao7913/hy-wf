package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 15:42
 **/
public interface FunctionService extends IService<Function> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Function function);

    /**
     * 修改用户
     */
    void update(Function function);

    List<Function> findByPositionAndAppType(String position,String appType);

    void deleteRedis(Function function);

    List<CommonService.OutPut> updateRedis(List<CommonService.OutPut> outPutList,Function function);
}
