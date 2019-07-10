package com.hy.wf.api.service.v1;

import com.hy.wf.entity.Vip;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:21
 **/

public interface VipService {

    /** 
    * @Description: 通过ID查询vip信息
    * @Param [id]
    * @return: com.hy.wf.dao.entity.v1.Vip
    * @Author: jt 
    * @Date: 2019/1/17 
    */ 
    Vip findVipById(Long id);

    /**
    * @Description: 查询vip列表
    * @Param []
    * @return: java.util.List<com.hy.wf.dao.entity.v1.Vip>
    * @Author: jt
    * @Date: 2019/1/17
    */
    List<Vip> findList(String appType);

}
