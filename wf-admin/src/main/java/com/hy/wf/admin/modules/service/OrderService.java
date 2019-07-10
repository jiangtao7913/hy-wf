package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Order;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 16:06
 **/
public interface OrderService extends IService<Order> {

    PageUtils queryPage(Map<String, Object> params);
    
}
