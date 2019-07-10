package com.hy.wf.api.service.pay;

import com.hy.wf.api.service.v1.AppConfigService;
import com.hy.wf.api.service.v1.OrderService;
import com.hy.wf.api.service.v1.PluginConfigService;
import com.hy.wf.entity.Order;
import com.hy.wf.entity.PluginConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description: 支付插件
 * @author: jt
 * @create: 2019-01-16 16:00
 **/
@Slf4j
public abstract class PaymentPlugin implements Comparable<PaymentPlugin>{

    /** 支付方式名称属性名称 */
    public static final String PAYMENT_NAME_ATTRIBUTE_NAME = "paymentName";

    /** LOGO属性名称 */
    public static final String LOGO_ATTRIBUTE_NAME = "logo";

    /** 描述属性名称 */
    public static final String DESCRIPTION_ATTRIBUTE_NAME = "description";

    @Autowired
    private PluginConfigService pluginConfigService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppConfigService appConfigService;

    /**
     * 请求方法
     */
    public enum RequestMethod {

        /** POST */
        post,

        /** GET */
        get
    }

    /**
     * 通知方法
     */
    public enum NotifyMethod {

        /** 通用 */
        general,

        /** 同步 */
        sync,

        /** 异步 */
        async
    }

    /**
     * 获取插件名称
     * @return
     */
    public final String getPluginName() {
        return getClass().getAnnotation(Component.class).value();
    }

    /**
     * 获取插件名称
     *
     * @return 名称
     */
    public abstract String getName();

    /**
     * 获取网址
     *
     * @return 网址
     */
    public abstract String getSiteUrl();

    /**
     * 获取是否已安装
     *
     * @return 是否已安装
     */
    public boolean getIsInstalled(String appType) {
        return pluginConfigService.pluginIdExists(getPluginName(),appType);
    }

    /**
     * 获取插件配置
     *
     * @return 插件配置
     */
    public PluginConfig getPluginConfig(String appType) {
        return pluginConfigService.findByPluginName(getPluginName(),appType);
    }

    /**
     * 获取是否已启用
     *
     * @return 是否已启用
     */
    public boolean getIsEnabled(String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        if(pluginConfig == null || pluginConfig.getIsEnabled() == PluginConfig.IsEnabled.unable.value){
            return false;
        }
        return true;
    }

    /**
     * 用于扩展获取支付方式名称
     *
     * @return 支付方式名称
     */
    public String getPaymentName(String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return pluginConfig != null ? pluginConfig.getAttribute(PAYMENT_NAME_ATTRIBUTE_NAME) : null;
    }

    /**
     * 用于扩展获取LOGO
     *
     * @return LOGO
     */
    public String getLogo(String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return pluginConfig != null ? pluginConfig.getAttribute(LOGO_ATTRIBUTE_NAME) : null;
    }

    /**
     * 用于扩展获取描述
     *
     * @return 描述
     */
    public String getDescription(String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return pluginConfig != null ? pluginConfig.getAttribute(DESCRIPTION_ATTRIBUTE_NAME) : null;
    }


    /**
     * 获取请求URL
     *
     * @return 请求URL
     */
    public abstract String getRequestUrl();

    /**
     * 获取请求方法
     *
     * @return 请求方法
     */
    public abstract RequestMethod getRequestMethod();

    /**
     * 获取请求字符编码
     *
     * @return 请求字符编码
     */
    public abstract String getRequestCharset();

    /**
     * 获取请求参数
     *
     * @param orderNumber 编号
     * @param description 描述
     * @param request httpServletRequest
     * @return 请求参数
     */
    public abstract Map<String, Object> getParameterMap(String orderNumber, String description, HttpServletRequest request,String appType);

    /**
     * 验证通知是否合法
     *
     * @param orderNumber 编号
     * @param notifyMethod 通知方法
     * @param request httpServletRequest
     * @param response HttpServletResponse
     * @param parameters
     * @return 通知是否合法
     */
    public abstract Map<String, Object> verifyNotify(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters,String appType);

    /**
     * 获取通知返回消息
     *
     * @param orderNumber 编号
     * @param notifyMethod 通知方法
     * @param request httpServletRequest
     * @return 通知返回消息
     */
    public abstract String getNotifyMessage(String orderNumber, NotifyMethod notifyMethod, HttpServletRequest request);

    /**
     * 用户扩展获取超时时间
     *
     * @return 超时时间
     */
    public abstract Integer getTimeout();

    /**
     * 支付
     */
    public abstract Map<String, Object> pay(String orderNumber, String description, BigDecimal amount, HttpServletRequest request,String appType);

    /**
     * 根据订单编号查询订单
     * @param orderNumber 订单编号
     * @return 收款单，若不存在则返回null
     */
    protected Order getOrderByOrderNumber(String orderNumber) {
        return orderService.findByOrderNumber(orderNumber);
    }

    /**
     * 获取通知URL
     *
     * @param orderNumber 编号
     * @param notifyMethod 通知方法
     * @return 通知URL
     */
    protected String getNotifyUrl(String orderNumber, NotifyMethod notifyMethod,String appType) {
       // appConfigService.setValue("pay","site.url", "http://api.qtb.hongyunapp.com/rest");
        String siteUrl = appConfigService.getValue("pay","site.url",appType);
        String url = siteUrl + "/pay/" + getPluginName() +  "/notify";
        if (notifyMethod == null) {
            url = url + "/" + NotifyMethod.general + "/" + orderNumber+"/"+appType;
        } else {
            url = url + "/" + notifyMethod + "/" + orderNumber+"/"+appType;
        }
        log.info("getNotifyUrl:{}", url);
        return url;
    }

    /**
     * 连接Map键值对
     *
     * @param map  Map
     * @param prefix 前缀
     * @param suffix 后缀
     * @param separator  连接符
     * @param ignoreEmptyValue 忽略空值
     * @param ignoreKeys 忽略Key
     * @return 字符串
     */
    protected String joinKeyValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
        List<String> list = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (StringUtils.isNotEmpty(key)
                    &&!ArrayUtils.contains(ignoreKeys, key)
                    &&(!ignoreEmptyValue || StringUtils.isNotEmpty(value))
                ) {
                    list.add(key + "=" + (value != null ? value : ""));
                }
            }
        }
        String value= (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
        return value;
    }

    /**
     * 连接Map值
     *
     * @param map Map
     * @param prefix 前缀
     * @param suffix 后缀
     * @param separator 连接符
     * @param ignoreEmptyValue 忽略空值
     * @param ignoreKeys 忽略Key
     * @return 字符串
     */
    protected String joinValue(Map<String, Object> map, String prefix, String suffix, String separator, boolean ignoreEmptyValue, String... ignoreKeys) {
        List<String> list = new ArrayList<String>();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = ConvertUtils.convert(entry.getValue());
                if (StringUtils.isEmpty(key) && !ArrayUtils.contains(ignoreKeys, key) && (!ignoreEmptyValue || StringUtils.isEmpty(value))) {
                    list.add(value != null ? value : "");
                }
            }
        }

        return (prefix != null ? prefix : "") + StringUtils.join(list, separator) + (suffix != null ? suffix : "");
    }

    /**
     * POST请求
     *
     * @param url URL
     * @param postDataXML 请求参数
     * @return 返回结果
     */
    protected String post(String url, String postDataXML) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(postEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity, "utf-8");
            EntityUtils.consume(httpEntity);
        } catch (ClientProtocolException e) {
            log.error("post(url={}, postDataXML={})", url, postDataXML, e);
        } catch (IOException e) {
            log.error("post(url={}, postDataXML={})", url, postDataXML, e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取排序
     *
     * @return 排序
     */
    public Integer getOrder(String appType) {
        PluginConfig pluginConfig = getPluginConfig(appType);
        return pluginConfig != null ? pluginConfig.getOrders() : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        PaymentPlugin other = (PaymentPlugin) obj;
        return new EqualsBuilder().append(getPluginName(), other.getPluginName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getPluginName()).toHashCode();
    }

    @Override
    public int compareTo(PaymentPlugin paymentPlugin) {
        return 0;
    }

}
