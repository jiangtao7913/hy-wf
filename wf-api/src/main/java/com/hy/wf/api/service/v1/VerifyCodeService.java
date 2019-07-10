package com.hy.wf.api.service.v1;

import com.hy.wf.common.exception.ServiceException;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:18
 **/
public interface VerifyCodeService {

    /**
     * 生成验证码
     *
     * @param mobile 手机号
     * @param mobile IP
     * @return
     */
    String send(String mobile, String ip) throws ServiceException;

    /**
     * 校验手机验证码
     *
     * @param mobile 手机号
     * @param code 验证码
     * @return
     * @throws ServiceException
     */
    Boolean verify(String mobile, String code) throws ServiceException;

    String getMax(String mobile);

    String sentMax(String mobile,String max);


}
