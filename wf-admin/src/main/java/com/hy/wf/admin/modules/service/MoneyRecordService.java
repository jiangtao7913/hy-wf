package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.MoneyRecord;

import java.util.Map;

public interface MoneyRecordService extends IService<MoneyRecord> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    * @Description: 提现审核
    * @Param [id, status, reason]
    * @return: boolean
    * @Author: jt
    * @Date: 2019/3/27
    */
    boolean withdrawAudit(Long id,Integer status,String reason);

    /**
     * 发送提现短信消息(
     */
    void sendWithdrawMsg();



}
