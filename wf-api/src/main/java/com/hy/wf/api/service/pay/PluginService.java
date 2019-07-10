package com.hy.wf.api.service.pay;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:08
 **/
public interface PluginService {
    /**
     * 获取支付插件
     *
     * @return 支付插件
     */
    List<PaymentPlugin> getPaymentPlugins();

    /**
     * 获取支付插件
     *
     * @param isEnabled 是否启用
     * @return 支付插件
     */
    List<PaymentPlugin> getPaymentPlugins(boolean isEnabled,String appType);

    /**
     * 获取支付插件
     *
     * @param pluginName ID
     * @return 支付插件
     */
    PaymentPlugin getPaymentPlugin(String pluginName);


}
