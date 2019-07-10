package com.hy.wf.api.service.pay;

import com.hy.wf.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:余额支付插件
 * @author: jt
 * @create: 2019-03-02 16:46
 **/
@Component("balancePayPlugin")
@Slf4j
public class BalancePlugin extends PaymentPlugin{

    @Override
    public String getName() {
        return "余额支付";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.net";
    }

    @Override
    public String getRequestUrl() {
        return null;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.get;
    }

    @Override
    public String getRequestCharset() {
        return null;
    }

    @Override
    public Map<String, Object> getParameterMap(String orderNumber, String description, HttpServletRequest request,String appType) {
        return null;
    }

    @Override
    public Map<String, Object> verifyNotify(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters,String appType) {
        Map<String, Object> result = new HashMap<>(10);
        result.put("success", true);
        // 余额交易号
        result.put("tradeNo",orderNumber);
        // 付款人
        result.put("payer",parameters.get("name"));
        return result;
    }

    @Override
    public String getNotifyMessage(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request) {
        if (notifyMethod == NotifyMethod.async) {
            Order order = getOrderByOrderNumber(orderNumber);
            if (order != null && Order.PayStatus.success.value == order.getPayStatus() &&
                    Order.OrderStatus.completed.value == order.getOrderStatus()) {
                return "success";
            }
        }
        return null;
    }

    @Override
    public Integer getTimeout() {
        return null;
    }

    @Override
    public Map<String, Object> pay(String orderNumber, String description, BigDecimal amount, HttpServletRequest request,String appType) {
        Map<String, Object> map = new HashMap<>(10);
        map.put("success",true);
        return map;
    }
}
