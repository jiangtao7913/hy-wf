package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.service.v1.SmsService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:23
 **/
@Service
@Lazy(false)
public class SmsServiceImpl implements SmsService {
    @Value("${sms.url}")
    private String url;
    @Value("${sms.account}")
    private String account;
    @Value("${sms.password}")
    private String password;

    @Override
    public String send(String mobile, String message) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("account", account));
            nameValuePairs.add(new BasicNameValuePair("pswd", password));
            nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
            nameValuePairs.add(new BasicNameValuePair("needstatus", String.valueOf(true)));
            nameValuePairs.add(new BasicNameValuePair("msg", message));
            nameValuePairs.add(new BasicNameValuePair("product", null));
            nameValuePairs.add(new BasicNameValuePair("extno", null));

            HttpGet httpGet = new HttpGet(url + "HttpBatchSendSM" + (StringUtils.contains(url, "?") ? "&" : "?") + EntityUtils.toString(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")));
            HttpResponse httpResponse = httpClient.execute(httpGet);
            Integer statusCode = httpResponse.getStatusLine().getStatusCode();
            result = statusCode.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
