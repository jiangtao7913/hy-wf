package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Order;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 17:52
 **/
public interface OrderRepository extends BaseRepository<Order,Long> {

    Order findByOrderNumber(String orderNumber);

    int updateOrderById(Order order);

    List<Order> findOrderByLimit(int limit);
}
