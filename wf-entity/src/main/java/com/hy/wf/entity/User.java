package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 17:46
 **/
@TableName("t_user")
@Data
public class User extends BaseEntity {
    private static final long serialVersionUID = 4937031590979943324L;

    private String uid;

    /**
     * 用户充值总金额
     */
    private BigDecimal rechargeTotal;
    /**
     * 用户来源
     */
    private String source;

    /**
     * 是否在线
     */
    private int online;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 用户设备的key
     */
    private String hardwareKey;

    /**
     * 用户关键字
     */
    private String sign;

    /**
     * 账号类型
     */
    private int accountType;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码
     */
    private String sex;

    /**
     * 用户余额
     */
    private BigDecimal balance;


    private String appType;


    public enum AccountType {
        WX(0,"微信"),
        QQ(1,"QQ"),
        Phone(2,"手机"),
        Virtual(3,"虚拟用户")
        ;
        public final  int value;
        public final String remark;

        AccountType(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }



    public enum Online {
        n(0,"离线"),
        y(1,"在线"),
        ;
        public final  int value;
        public final String remark;

        Online(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }
}
