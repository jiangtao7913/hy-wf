package com.hy.wf.api.service.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.PluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @program: hy-wf
 * @description: 支付宝支付
 * @author: jt
 * @create: 2019-01-16 15:54
 **/
@Component("alipayPlugin")
@Slf4j
public class AlipayPlugin extends PaymentPlugin {

    @Override
    public String getName() {
        return "支付宝(即时交易)";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.net";
    }

    @Override
    public String getRequestUrl() {
        return "https://openapi.alipay.com/gateway.do";
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.get;
    }

    @Override
    public String getRequestCharset() {
        return "UTF-8";
    }

    /**
     * 请求参数
     *
     * @param orderNumber
     * @param description 描述
     * @param request     httpServletRequest
     * @return
     */
    @Override
    public Map<String, Object> getParameterMap(String orderNumber, String description, HttpServletRequest request,String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        Order order = getOrderByOrderNumber(orderNumber);
        Map<String, Object> parameterMap = new HashMap<>(10);
        //应用id
        parameterMap.put("app_id", pluginConfig.getAttribute("appId"));
        //接口名称
        parameterMap.put("method", "alipay.trade.app.pay");
        //仅支持JSON
        parameterMap.put("format", "json");
        //订单总金额
        parameterMap.put("total_amount", order.getOrderPrice().setScale(2, RoundingMode.HALF_UP));
        //请求使用的编码格式
        parameterMap.put("charset", "utf-8");
        //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
        parameterMap.put("sign_type", "RSA2");
        //支付宝服务器主动通知商户服务器里指定的页面http/https路径(回调地址)
        parameterMap.put("notify_url", getNotifyUrl(orderNumber, NotifyMethod.async,appType));
        //商户网站唯一订单号
        parameterMap.put("out_trade_no", orderNumber);
        //商品的标题/交易标题/订单标题/订单关键字等。
        parameterMap.put("subject", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
        //对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        parameterMap.put("body", StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 600));
        //商户请求参数的签名串，详见签名
        parameterMap.put("sign", generateSign(parameterMap,appType));
        return parameterMap;
    }


    /**
     * 验证通知是否合法
     *
     * @param orderNumber  编号
     * @param notifyMethod 通知方法
     * @param request      httpServletRequest
     * @param response     HttpServletResponse
     * @param parameters
     * @return
     */
    @Override
    public Map<String, Object> verifyNotify(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters,String appType) {
        Map<String, Object> result = new HashMap<>(10);
        PluginConfig pluginConfig = getPluginConfig(appType);
        //调用SDK验证签名
        Order order = getOrderByOrderNumber(orderNumber);
        try {
            if (pluginConfig.getAttribute("appId").equals(parameters.get("app_id"))
                    && orderNumber.equals(parameters.get("out_trade_no"))
                    && ("TRADE_SUCCESS".equals(parameters.get("trade_status")) || "TRADE_FINISHED".equals(parameters.get("trade_status")))
                    && order.getOrderPrice().compareTo(new BigDecimal(parameters.get("total_amount"))) == 0
                    && AlipaySignature.rsaCheckV1(parameters, pluginConfig.getAttribute("alipayPublicKey"), "utf-8", "RSA2")
            ) {
                result.put("success", true);
                // 支付宝交易号
                result.put("tradeNo", parameters.get("trade_no"));
                // 付款人
                result.put("payer", parameters.get("buyer_logon_id"));
            } else {
                result.put("success", false);
            }
        } catch (AlipayApiException e) {
            log.error("verifyNotify(sn={})", orderNumber, e);
            result.put("success", false);
        }
        return result;
    }

    /**
     * 获取通知的返回消息
     *
     * @param orderNumber
     * @param notifyMethod 通知方法
     * @param request      httpServletRequest
     * @return
     */
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
        return 21600;
    }

    @Override
    public Map<String, Object> pay(String orderNumber, String description, BigDecimal amount, HttpServletRequest request,String appType) {
        // 实例化客户端
        PluginConfig pluginConfig = getPluginConfig(appType);
        String appId = pluginConfig.getAttribute("appId");
        String charset = "utf-8";
        AlipayClient alipayClient = new DefaultAlipayClient(getRequestUrl(), appId, pluginConfig.getAttribute("appPrivateKey"), "json", charset, pluginConfig.getAttribute("alipayPublicKey"), "RSA2");
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        // 商品标题
        model.setSubject(StringUtils.abbreviate(description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 60));
        model.setOutTradeNo(orderNumber);
        //超时关闭该订单时间
        model.setTimeoutExpress("15d");
        // 订单总金额
        model.setTotalAmount(amount.setScale(2, RoundingMode.HALF_UP).toString());
        // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayTradeAppPayRequest.setBizModel(model);
        // 回调地址
        alipayTradeAppPayRequest.setNotifyUrl(getNotifyUrl(orderNumber, NotifyMethod.async,appType));
        Map<String, Object> map = new HashMap<>(10);
        String orderStr = "";
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(alipayTradeAppPayRequest);
            // 就是orderString 可以直接给客户端请求，无需再做处理。
            orderStr = response.getBody();
            map.put("orderStr", orderStr);
            log.info("orderStr={}", orderStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 生成签名
     *
     * @param parameterMap 参数
     * @return 签名
     */
    private String generateSign(Map<String, ?> parameterMap,String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<>(parameterMap), null, pluginConfig.getAttribute("key"), "&", true, "sign_type", "sign"));
    }


}
