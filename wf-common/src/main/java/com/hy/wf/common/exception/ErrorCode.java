package com.hy.wf.common.exception;

public enum ErrorCode {
    C404("404","接口地址错误"),
    C400("400","客户端参数错误"),
    C401("401","请求方式错误"),
    C500("500","服务器内部错误"),

    C1000("1000","token不能为空"),
    C1001("1001","token无效，请重新登录"),
    C1003("1003","临时令牌失效"),
    C1006("1006","验证码不正确"),
    C1007("1007","今日发送验证码到达上限"),

    C2001("2001","版本信息不存在"),

    C3001("3001","手机号未注册"),
    C3002("3002","密码错误"),
    C3003("3003","手机号已被注册"),
    C3004("3004","上传失败"),
    C3005("3005","用户不存在"),


    C4001("4001","机器唯一码不存在"),

    C5001("5001","余额不足，无法支付"),
    C5002("5002","订单错误"),

    C6001("6001","免费使用次数已用完"),

    C7001("7001","不能自己邀请自己"),
    C7002("7002","不能重复邀请"),
    C7003("7003","不能互相邀请"),
    C7004("7004","余额不足,不能提现"),
    C7005("7005","提现金额太小，不能提现"),
    C7006("7006","邀请码不存在"),
    ;
    /** 代码 */
    private String code;

    /** 消息 */
    private String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message != null && !"".equals(message) ? code + "-" + message : code;
    }

}
