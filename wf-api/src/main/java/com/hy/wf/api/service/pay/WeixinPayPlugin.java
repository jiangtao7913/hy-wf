package com.hy.wf.api.service.pay;

import com.hy.wf.common.util.IPUtils;
import com.hy.wf.common.util.XMLParser;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.PluginConfig;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: hy-wf
 * @description: 微信支付
 * @author: jt
 * @create: 2019-01-22 09:58
 **/
@Component("weixinPayPlugin")
@Slf4j
public class WeixinPayPlugin extends PaymentPlugin{

    @Override
    public String getName() {
        return "微信(即时交易)";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.net";
    }

    @Override
    public String getRequestUrl() {
        return "https://api.mch.weixin.qq.com/pay/unifiedorder";
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.get;
    }

    @Override
    public String getRequestCharset() {
        return "UTF-8";
    }

    @Override
    public Map<String, Object> getParameterMap(String orderNumber, String description, HttpServletRequest request,String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        Order order = getOrderByOrderNumber(orderNumber);
        Date start = new Date();
        Date end = DateUtils.addDays(start, 15);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SortedMap<String, Object> parameterMap = new TreeMap<>();
        // 应用APPID
        parameterMap.put("appid", pluginConfig.getAttribute("appId"));
        // 商户号
        parameterMap.put("mch_id", pluginConfig.getAttribute("mchId"));
        // 签名类型
        parameterMap.put("sign_type", "MD5");
        // 随机字符串
        parameterMap.put("nonce_str", RandomStringUtils.random(32, "abcdefghijklmnopqrstuvwxyz0123456789"));
        parameterMap.put("notify_url", getNotifyUrl(orderNumber, NotifyMethod.async,appType));
        parameterMap.put("out_trade_no", orderNumber);
        // 商品描述
        parameterMap.put("body", StringUtils.abbreviate(pluginConfig.getAttribute("appName") + "-" + description.replaceAll("[^0-9a-zA-Z\\u4e00-\\u9fa5 ]", ""), 30));
        // 单位为分
        parameterMap.put("total_fee", order.getOrderPrice().multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toString());
        //交易类型 app支付必须填写为APP
        parameterMap.put("trade_type", "APP");
        parameterMap.put("spbill_create_ip", IPUtils.getIpAddr(request));
        parameterMap.put("attach", pluginConfig.getAttribute("appName"));
        //交易起始时间
        parameterMap.put("time_start", dateFormat.format(start));
        //交易结束时间
        parameterMap.put("time_expire", dateFormat.format(end));
        parameterMap.put("sign", generateSign(parameterMap,appType));
        return parameterMap;
    }

    /**
     * 生成签名
     *
     * @param parameterMap 参数
     * @return 签名
     */
    private String generateSign(Map<String, ?> parameterMap,String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return DigestUtils.md5Hex(joinKeyValue(new TreeMap<>(parameterMap), null, "&key=" + pluginConfig.getAttribute("key"), "&", true, "key", "sign")).toUpperCase();
    }

    /**
     * 验证通知是否合法
     * @param orderNumber 编号
     * @param notifyMethod 通知方法
     * @param request httpServletRequest
     * @param response HttpServletResponse
     * @param parameters
     * @return
     */
    @Override
    public Map<String, Object> verifyNotify(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters,String appType) {
        Map<String, Object> result = Collections.emptyMap();
        String wxResultXml = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            inputStream = request.getInputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            // xml数据
            wxResultXml = new String(byteArrayOutputStream.toByteArray(), "utf-8");

        } catch (UnsupportedEncodingException e) {
            log.error("verifyNotify(sn={})", e);
        } catch (IOException e) {
            log.error("verifyNotify(sn={})", e);
        }finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("verifyNotify:wxResultXml=" + wxResultXml);
        if (StringUtils.isNotEmpty(wxResultXml)) {
            result = new HashMap<>(1);
            Map<String, Object> wxResultMap = XMLParser.fromXML(wxResultXml);
            String signFromAPIResponse = (String) wxResultMap.get("sign");
            if(StringUtils.isEmpty(signFromAPIResponse)){
                log.warn("API返回的数据签名数据不存在，有可能被第三方篡改!");
                result.put("success", false);
            } else {
                //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
                String signForAPIResponse = generateSign(wxResultMap,appType);
                if(StringUtils.equals(signForAPIResponse, signFromAPIResponse)) {
                    result.put("success", true);
                    // 微信交易号
                    result.put("tradeNo", wxResultMap.get("transaction_id"));
                    // 微信openid
                    result.put("payer", wxResultMap.get("openid"));
                    log.info("API返回的数据签名验证通过!");
                } else {
                    //签名验不过，表示这个API返回的数据有可能已经被篡改了
                    result.put("success", false);
                    log.warn("API返回的数据签名验证不通过，有可能被第三方篡改!");
                }
            }
        }
        return result;
    }

    /**
     * 获取通知返回消息
     * @param orderNumber 编号
     * @param notifyMethod 通知方法
     * @param request httpServletRequest
     * @return
     */
    @Override
    public String getNotifyMessage(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request) {
        if (notifyMethod == NotifyMethod.async) {
            Order order = getOrderByOrderNumber(orderNumber);
            if (order != null && Order.PayStatus.success.value == order.getPayStatus()
                && Order.OrderStatus.completed.value == order.getOrderStatus()) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            } else {
                return null;
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
        Map<String, Object> parameterMap = getParameterMap(orderNumber, description, request,appType);
        parameterMap.remove("key");
        XStream xStream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
        xStream.alias("xml", TreeMap.class);
        xStream.registerConverter(new MapEntryConverter());
        String xml = xStream.toXML(parameterMap);
        xml = xml.replace("<body>", "<body><![CDATA[").replace("</body>", "]]></body>");
        String result = post(getRequestUrl(), xml);
        parameterMap.clear();
        Map<String, Object> resultMap = XMLParser.fromXML(result);
        String prepayId = (String) resultMap.get("prepay_id");
        if (StringUtils.isNotEmpty(prepayId)) {
            PluginConfig pluginConfig = getPluginConfig(appType);
            // 预付单ID
            parameterMap.put("prepayid", prepayId);
            // 应用APPID
            parameterMap.put("appid", pluginConfig.getAttribute("appId"));
            // 商户号
            parameterMap.put("partnerid", pluginConfig.getAttribute("mchId"));
            // 随机字符串
            parameterMap.put("noncestr", RandomStringUtils.random(32, "abcdefghijklmnopqrstuvwxyz0123456789"));
            // 时间戳10位
            parameterMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
            parameterMap.put("package", "Sign=WXPay");
            parameterMap.put("sign", generateSign(parameterMap,appType));
        } else {
            parameterMap = resultMap;
        }
        return parameterMap;
    }

    /**
     * xStream Map类转换器
     */
    public static class MapEntryConverter implements Converter {
        @Override
        public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
            return AbstractMap.class.isAssignableFrom(type);
        }
        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            String value;
            @SuppressWarnings("unchecked")
            AbstractMap<Object, Object> map = (AbstractMap<Object, Object>) source;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                writer.startNode((String) entry.getKey());
                value = "";
                if (entry.getValue() != null) {
                    value = entry.getValue().toString();
                }
                writer.setValue(value);
                writer.endNode();
            }
        }
        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Map<String, String> map = new HashMap<>(1);
            while(reader.hasMoreChildren()) {
                reader.moveDown();
                map.put(reader.getNodeName(), reader.getValue());
                reader.moveUp();
            }
            return map;
        }
    }

}

