package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.VerifyCodeRepository;
import com.hy.wf.api.service.v1.SmsService;
import com.hy.wf.api.service.v1.VerifyCodeService;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.common.util.VerifyCodeUtils;
import com.hy.wf.entity.VerifyCode;
import com.hy.wf.entity.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:20
 **/
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private VerifyCodeRepository verifyCodeRepository;
    @Autowired
    private SmsService smsService;

    @Override
    public String send(String mobile, String ip) throws ServiceException {
        String code = VerifyCodeUtils.generateVerifyCode(6, "23456789");
        smsService.send(mobile, "【红运科技】您的验证码为" + code + ",请在15分钟内按规定填写。");
        verifyCodeRepository.save(new VerifyCode(true, mobile, code, ip, null));
        return code;
    }

    @Override
    public Boolean verify(String mobile, String code) throws ServiceException {
        Boolean valid = false;
        VerifyCode verifyCode = verifyCodeRepository.findByMobileCodeAndTime(mobile, code, System.currentTimeMillis(), BaseEntity.DataStatus.valid);
        if (verifyCode != null) {
            valid = true;
            //verifyCode.setDataStatus(BaseEntity.DataStatus.invalid.value);
            verifyCodeRepository.update(verifyCode.getId(),BaseEntity.DataStatus.invalid);
        }
        return valid;
    }

    /**
     * 从缓存中获取数据
     * @param mobile
     * @return
     */
    @Cacheable(value = "sendMax",key = "#mobile")
    @Override
    public String getMax(String mobile) {
        return "0-0";
    }

    /**
     * 设置缓存
     * @param mobile
     * @param max
     * @return
     */
    @CachePut(value = "sendMax",key = "#mobile")
    @Override
    public String sentMax(String mobile, String max) {
        return max;
    }


}
