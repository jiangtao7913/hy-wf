package com.hy.wf.api.service.v1;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:22
 **/
public interface SmsService {
    /**
     * 发送
     *
     * @param mobile
     * @param message
     * @return
     */
    String send(String mobile, String message);
}
