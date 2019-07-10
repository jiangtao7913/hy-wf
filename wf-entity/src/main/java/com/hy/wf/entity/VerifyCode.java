package com.hy.wf.entity;

import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-02-27 15:28
 **/
@Data
public class VerifyCode extends BaseEntity {

    private static final long serialVersionUID = -1801517526451723466L;

    private String mobile;

    private String code;

    private String request;

    private long expiryTime;

    private String memo;

    public VerifyCode() {

    }

    public VerifyCode(boolean initialize, String mobile, String code, String request, String memo) {
        if (initialize) {
            super.init();
        }
        this.mobile = mobile;
        this.code = code;
        this.request = request;
        this.memo = memo;
        this.expiryTime =  super.getCreateDate().getTime() + 600000;
    }

}
