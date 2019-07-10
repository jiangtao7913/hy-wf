package com.hy.wf.api.web.v1;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.service.pay.PaymentPlugin;
import com.hy.wf.api.service.pay.PluginService;
import com.hy.wf.api.service.v1.OrderService;
import com.hy.wf.api.service.v1.SnService;
import com.hy.wf.api.service.v1.VipService;
import com.hy.wf.api.web.BaseController;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.Sn;
import com.hy.wf.entity.User;
import com.hy.wf.entity.Vip;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:26
 **/
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController extends BaseController {

    @Autowired
    private PluginService pluginService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private VipService vipService;

    /**
     * 获取支付插件列表
     *
     * @return
     */
    @PostMapping("/plugins")
    @IsLogin
    @CustomLog
    public Result getPaymentPlugins(@RequestHeader("appType")String appType) {
        List<Map> mapList = new ArrayList<>();
        List<PaymentPlugin> paymentPlugins = pluginService.getPaymentPlugins(true,appType);
        for (PaymentPlugin paymentPlugin : paymentPlugins) {
            Map<String, String> map = new HashMap<>(1);
            //插件图标
            map.put("logo", paymentPlugin.getLogo(appType));
            //插件名称
            map.put("pluginName", paymentPlugin.getPluginName());
            mapList.add(map);
        }
        return Result.success(mapList);
    }

    /**
     * 查询vip信息
     *
     * @return
     */
    @PostMapping("/getVipInfo")
    @IsLogin
    @CustomLog
    public Result getVipList(@RequestHeader("appType")String appType) {
        //查询vip信息
        List<Vip> vipList = vipService.findList(appType);
        //查询最近用户付费信息
        List<Order> orderList = orderService.findOrderByLimit(10);
        Map<String,Object> map = new HashMap<>(2);
        map.put("vipList",vipList);
        map.put("orderList",orderList);
        return Result.success(map);
    }

    /**
     * 下单接口
     *
     * @return
     */
    @PostMapping("/{type}/{pluginName}/{id}")
    @CustomLog
    public Result produceOrder(@PathVariable("type") Integer type, @PathVariable("pluginName") String pluginName, @PathVariable("id") Long id,@RequestHeader("appType")String appType) {
        Order.Type mType = Order.Type.valueOf(type);
        if (null == mType) {
            return Result.fail(ErrorCode.C400);
        }
        return orderService.produceOrder(mType, pluginName, id, getCurrentUser(), request,appType);
    }

    /**
     * 支付宝回调接口
     */
    @PostMapping(value = "/alipayPlugin/notify/{notifyMethod}/{orderNumber}/{appType}", consumes = "application/x-www-form-urlencoded")
    @IsLogin
    @CustomLog
    public Result aliNotify(@PathVariable("notifyMethod") PaymentPlugin.NotifyMethod notifyMethod, @PathVariable("orderNumber") String orderNumber,
                            HttpServletRequest request, HttpServletResponse response,@PathVariable("appType")String appType) {
        log.warn("----支付宝回调接口"+request.getParameterMap());
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            parameters.put(name,valueStr);
        }

//        Map.Entry<String, List<String>> entry;
//        Iterator<Map.Entry<String, List<String>>> iterator = formParams.entrySet().iterator();
//        String value = null;
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            value = entry.getValue().toString();
//            if (StringUtils.isNotEmpty(value)) {
//                parameters.put(entry.getKey(), value.substring(1, value.length() - 1));
//            }
//        }
//        String result = null;
//        Order order = orderService.findByOrderNumber(orderNumber);
//        if (order != null && Order.PayStatus.wait.value == order.getPayStatus()) {
//            //获取支付宝插件
//            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(order.getPluginName());
//            if (paymentPlugin != null) {
//                Map<String, Object> resultMap = paymentPlugin.verifyNotify(orderNumber, notifyMethod, request, response, parameters);
//                if ((Boolean) resultMap.get("success")) {
//                    result = setPro(order, resultMap, result, paymentPlugin, orderNumber, notifyMethod, response, request);
//                }
//            }
//        }
        String result = payResult(notifyMethod,orderNumber,response,parameters,appType);
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",result);
        return Result.success(map);
    }

    /**
     * 微信回调接口
     */
    @PostMapping(value = "/weixinPayPlugin/notify/{notifyMethod}/{orderNumber}/{appType}")
    @IsLogin
    @CustomLog
    public Result weixinNotify(@PathVariable("notifyMethod") PaymentPlugin.NotifyMethod notifyMethod, @PathVariable("orderNumber") String orderNumber,
                               HttpServletResponse response,@PathVariable("appType")String appType) {
//        String result = null;
//        Order order = orderService.findByOrderNumber(orderNumber);
//        if (order != null && Order.PayStatus.wait.value == order.getPayStatus()) {
//            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(order.getPluginName());
//            if (paymentPlugin != null) {
//                Map<String, Object> resultMap = paymentPlugin.verifyNotify(orderNumber, notifyMethod, request, response, parameters);
//                if ((Boolean) resultMap.get("success")) {
//                    result = setPro(order, resultMap, result, paymentPlugin, orderNumber, notifyMethod, response, request);
//                }
//            }
//        }
        String result = payResult(notifyMethod,orderNumber,response,null,appType);
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",result);
        return Result.success(map);
    }

    /**
     * 余额支付回调接口
     */
    @PostMapping(value = "/balancePayPlugin/notify/{notifyMethod}/{orderNumber}/{appType}")
    @CustomLog
    public Result balanceNotify(@PathVariable("notifyMethod") PaymentPlugin.NotifyMethod notifyMethod, @PathVariable("orderNumber") String orderNumber,
                               HttpServletResponse response,@PathVariable("appType")String appType) {
        User user = getCurrentUser();
        Map<String, String> parameters = new HashMap<>(1);
        parameters.put("name",user.getName());
        String result = payResult(notifyMethod,orderNumber,response,parameters,appType);
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",result);
        return Result.success(map);
    }


    /** 
    * @Description: 共有支付回调方法处理
    * @Param [notifyMethod, orderNumber, response, parameters]
    * @return: java.lang.String 
    * @Author: jt 
    * @Date: 2019/3/4 
    */ 
    private String payResult(PaymentPlugin.NotifyMethod notifyMethod ,String orderNumber,HttpServletResponse response,Map<String, String> parameters,String appType){
        String result = null;
        Order order = orderService.findByOrderNumber(orderNumber);
        if (order != null && Order.PayStatus.wait.value == order.getPayStatus()) {
            PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(order.getPluginName());
            if (paymentPlugin != null) {
                Map<String, Object> resultMap = paymentPlugin.verifyNotify(orderNumber, notifyMethod, request, response, parameters,appType);
                if ((Boolean) resultMap.get("success")) {
                    result = setPro(order, resultMap, result, paymentPlugin, orderNumber, notifyMethod, response, request,appType);
                }
            }
        }
        return result;
    }

    /**
     * 共有支付回调方法处理
     */
    private String setPro(Order order, Map<String, Object> resultMap, String result, PaymentPlugin paymentPlugin, String orderNumber,
                          PaymentPlugin.NotifyMethod notifyMethod, HttpServletResponse response, HttpServletRequest request,String appType) {
//        try {
            order.setPayNumber(((String) resultMap.get("tradeNo")));
            order.setPayer((String) resultMap.get("payer"));
            order.setPaymentDate(new Date());
            if ((Boolean) resultMap.get("success")) {
                orderService.handle(order,appType);
                result = paymentPlugin.getNotifyMessage(orderNumber, notifyMethod, request);
//                if (StringUtils.isNotEmpty(result)) {
//                    PrintStream printStream = new PrintStream(response.getOutputStream());
//                    printStream.print(result);
//                    printStream.flush();
//                }
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//       }
        return result;
    }

    /**
     * 支付成功校验接口
     */
    @PostMapping("/success/{orderNumber}")
    @CustomLog
    public Result success(@PathVariable("orderNumber") String orderNumber, @RequestHeader("source")String source,
                          @RequestHeader("appType")String appType) {
        return orderService.success(getCurrentUser(),orderNumber,source,appType);
    }

}
