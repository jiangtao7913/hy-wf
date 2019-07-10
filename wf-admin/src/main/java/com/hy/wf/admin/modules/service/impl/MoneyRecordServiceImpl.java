package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.common.utils.SmsService;
import com.hy.wf.admin.modules.dao.MoneyRecordDao;
import com.hy.wf.admin.modules.service.MoneyRecordService;
import com.hy.wf.admin.modules.service.UserService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.MoneyRecord;
import com.hy.wf.entity.User;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 17:55
 **/
@Service
public class MoneyRecordServiceImpl extends ServiceImpl<MoneyRecordDao, MoneyRecord> implements MoneyRecordService {
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String status = (String)params.get("status");
        String type = (String)params.get("type");
        String userId = (String)params.get("userId");

        if(status != null && status.equals("100")){
            status = null;
        }
        if(type != null && type.equals("100")){
            type = null;
        }

        Page<MoneyRecord> page = this.selectPage(
                new Query<MoneyRecord>(params).getPage(),
                new EntityWrapper<MoneyRecord>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .eq(StringUtils.isNotBlank(status),"status", status)
                        .eq(StringUtils.isNotBlank(type),"type", type)
                        .eq(StringUtils.isNotBlank(userId),"user_id", userId)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawAudit(Long id,Integer status,String reason) {
        //审核不通过，返回用户余额
        MoneyRecord moneyRecord = this.selectById(id);
        Long userId = moneyRecord.getUserId();
        User user = userService.selectById(userId);
        BigDecimal balance = user.getBalance().add(moneyRecord.getAmount());
        user.setBalance(balance);
        userService.update(user);

        moneyRecord.setStatus(status);
        moneyRecord.setReason(reason);
        return this.updateById(moneyRecord);
    }

    @Override
    public void sendWithdrawMsg() {
        List<MoneyRecord> moneyRecordList =  this.selectList(new EntityWrapper<MoneyRecord>()
                .eq("status", MoneyRecord.Status.Audit.value)
                .eq("type", MoneyRecord.Type.Withdraw.value)
                .eq("data_status", BaseEntity.DataStatus.valid)
        );
        String message = "";
        for(MoneyRecord moneyRecord :moneyRecordList){
            message = message + "支付宝账号为：" + moneyRecord.getAccount()+" 姓名为："+moneyRecord.getName()
                    +"提现金额：" + moneyRecord.getAmount()+";";
        }
        if(!StringUtils.isEmpty(message)){
            smsService.send("13723447972", message);
        }
    }
}
