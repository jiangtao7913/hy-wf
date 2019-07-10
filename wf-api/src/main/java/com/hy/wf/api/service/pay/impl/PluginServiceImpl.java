package com.hy.wf.api.service.pay.impl;

import com.hy.wf.api.service.pay.PaymentPlugin;
import com.hy.wf.api.service.pay.PluginService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 15:10
 **/
@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {

    @Autowired
    private List<PaymentPlugin> paymentPlugins = new ArrayList<>();
    @Autowired
    private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public List<PaymentPlugin> getPaymentPlugins() {
        Collections.sort(paymentPlugins);
        return paymentPlugins;
    }

    @Override
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public List<PaymentPlugin> getPaymentPlugins(boolean isEnabled,String appType) {
        List<PaymentPlugin> result = new ArrayList<>();
        CollectionUtils.select(paymentPlugins, object -> {
            PaymentPlugin paymentPlugin = (PaymentPlugin) object;
            return paymentPlugin.getIsEnabled(appType) == isEnabled;
        }, result);
        Collections.sort(result);
        return result;
    }

    @Override
    public PaymentPlugin getPaymentPlugin(String pluginName) {
        return paymentPluginMap.get(pluginName);
    }


}
