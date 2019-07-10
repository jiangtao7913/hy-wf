package com.hy.wf.api.service.v1;

import com.hy.wf.common.Result;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.FunctionRule;
import lombok.Data;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-02 15:12
 **/
public interface FunctionService {

    /**
     * 根据位置查询功能列表
     * @param position
     * @return
     */
    List<OutPut> getAllAndAppType(String position,String appType);

    /**
     * 根据id查询功能
     */
    Function findById(Long id);

    /**
     * 根据类型查找功能
     */
    List<Function> findByType(Function.Type type,String appType);

    /** 
    * @Description: 用户功能使用记录
    * @Param [userId, functionId, hardwareKey]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/11 
    */ 
    Result functionRecord(Long userId,Long functionId,String hardwareKey);


    Result checkVip(Long userId);

    @Data
    class OutPut{
        private Function function;
        private FunctionRule functionRule;
    }
}
