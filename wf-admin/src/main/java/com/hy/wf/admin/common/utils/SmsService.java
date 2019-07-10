package com.hy.wf.admin.common.utils;

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
