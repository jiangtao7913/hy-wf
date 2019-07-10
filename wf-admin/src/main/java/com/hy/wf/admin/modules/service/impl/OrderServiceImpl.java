package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.OrderDao;
import com.hy.wf.admin.modules.service.OrderService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.AppConfig;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 16:07
 **/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String userId = (String)params.get("userId");
        String orderStatus = (String)params.get("orderStatus");
        String payStatus = (String)params.get("payStatus");
        String type = (String)params.get("type");
        if(orderStatus !=null && orderStatus.equals("100")){
            orderStatus = null;
        }
        if(payStatus !=null && payStatus.equals("100")){
            payStatus = null;
        }
        if(type !=null && type.equals("100")){
            type = null;
        }
        Page<Order> page = this.selectPage(
                new Query<Order>(params).getPage(),
                new EntityWrapper<Order>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .eq(StringUtils.isNotBlank(userId),"user_id", userId)
                        .eq(StringUtils.isNotBlank(orderStatus),"order_status", orderStatus)
                        .eq(StringUtils.isNotBlank(payStatus),"pay_status", payStatus)
                        .eq(StringUtils.isNotBlank(type),"type", type)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

}
