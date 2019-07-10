package com.hy.wf.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: hy-wf
 * @description: 订单
 * @author: jt
 * @create: 2019-01-16 17:27
 **/
@TableName("t_order")
@Data
public class Order extends BaseEntity {
    private static final long serialVersionUID = -2221620587371427093L;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;
    /**
     * 订单状态
     */
    private int orderStatus;

    /**
     * 支付状态状态
     */
    private int payStatus;

    /**
     * 订单类型
     */
    private int type;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 收款单号
     */
    private String payNumber;

    /**
     * 业务id
     */
    private Long businessId;

    private String summary;

    private Long userId;

    private String pluginName;

    private String payer;

    private Date paymentDate;

    private String paymentName;

    private int payType;

    public enum PayType{

        alipayPlugin(0,"支付宝"),
        weixinPayPlugin(1,"微信"),
        balancePayPlugin(2,"余额"),
        ;
        public final  int value;
        public final String remark;

        PayType(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }



    public enum Type{
        Single(0,"购买单个功能"),
        Vip(1,"购买vip"),
        Virtual(2,"虚拟订单")
        ;
        public final  int value;
        public final String remark;

        Type(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
        public static Type valueOf(int value) {
            for (Type type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum OrderStatus{

        confirmed(0,"已确认"),
        completed(1,"已完成"),
        cancelled(2,"已取消"),
        ;
        public final  int value;
        public final String remark;

        OrderStatus(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }

    public enum PayStatus{
        wait(0,"等待支付"),
        success(1,"支付成功"),
        failure(2,"支付失败"),
        ;
        public final  int value;
        public final String remark;

        PayStatus(int value, String remark) {
            this.value = value;
            this.remark = remark;
        }
    }


//    public static String getOrderNumber(){
//        //最大支持1-9个集群机器部署
//        String machineId = "Q1";
//        int hashCodeV = UUID.randomUUID().toString().hashCode();
//        //有可能是负数
//        if(hashCodeV < 0) {
//            hashCodeV = - hashCodeV;
//        }
//        // 0 代表前面补充0
//        // 4 代表长度为4
//        // d 代表参数为正数型
//        return machineId + String.format("%015d", hashCodeV);
//    }


}
