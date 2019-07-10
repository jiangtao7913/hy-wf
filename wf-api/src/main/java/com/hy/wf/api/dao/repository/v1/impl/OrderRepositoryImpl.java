package com.hy.wf.api.dao.repository.v1.impl;

import com.hy.wf.api.dao.base.BaseRepositoryImpl;
import com.hy.wf.api.dao.base.Condition;
import com.hy.wf.api.dao.repository.v1.OrderRepository;
import com.hy.wf.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 17:53
 **/
@Repository
public class OrderRepositoryImpl extends BaseRepositoryImpl<Order,Long> implements OrderRepository {
    private static final String TABLE_NAME = "t_order";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Order> getBeanClass() {
        return Order.class;
    }


    @Override
    public Order findByOrderNumber(String orderNumber) {
        return select().where(Condition.equal("order_number",orderNumber)).comm().findOne();
    }

    @Override
    public int updateOrderById(Order order) {
        return update().set("pay_number",order.getPayNumber()).set("payer",order.getPayer())
                .set("order_status",order.getOrderStatus()).set("pay_status",order.getPayStatus())
                .set("modify_date",order.getModifyDate()).set("payment_date",order.getPaymentDate())
                .where(Condition.equal("id",order.getId())).update();
    }

    @Override
    public List<Order> findOrderByLimit(int limit) {
        return select().where(Condition.equal("order_status",Order.OrderStatus.completed.value)).
                where(Condition.equal("pay_status",Order.PayStatus.success.value))
                .comm().orderBy("create_date",false).limit(limit).find();
    }

}
