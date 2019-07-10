package com.hy.wf.api.service.v1;

import com.hy.wf.common.Result;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 17:56
 **/
public interface OrderService {

    /**
     * 通过订单编号查询订单
     * @param orderNumber
     * @return
     */
    Order findByOrderNumber(String orderNumber);

    /** 
    * @Description: 生成订单
    * @Param [type, pluginName, id, user]
    * @return: java.util.Map<java.lang.String,java.lang.String> 
    * @Author: jt 
    * @Date: 2019/1/17 
    */ 
   Result produceOrder(Order.Type type, String pluginName, Long id, User user, HttpServletRequest request,String appType);


    /**
     * 支付处理
     *
     * @param order 收款单
     */
    void handle(Order order,String appType);


    /**
     * 前n个查找用户订单
     */
    List<Order> findOrderByLimit(int limit);

    /**
     * 订单支付成功检测接口
     */
    Result success(User user,String orderNumber,String source,String appType);
}
