package com.hy.wf.api.service.v1;

import com.hy.wf.common.Result;
import com.hy.wf.entity.InvitationDetail;
import com.hy.wf.entity.User;

import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-06 16:10
 **/
public interface InvitationService {
    
    /** 
    * @Description: 查询邀请信息
    * @Param [userId]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/12 
    */ 
    Result findInvitationInfo(User user,String appType);
    
    /** 
    * @Description: 填写邀请码 
    * @Param [user, code]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/12 
    */ 
    Result fillCode(User user, String code);
    
    /** 
    * @Description: 查看粉丝信息 
    * @Param [user, type]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/13 
    */ 
    Result getFansInfo(User user, InvitationDetail.Type type,String appType,Integer offset,Integer limit);

    /** 
    * @Description: 查看收入记录 
    * @Param [user, offset, limit]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/13 
    */ 
    Result getIncomeInfo(User user,Integer offset,Integer limit);

    /** 
    * @Description: 查看支出记录 
    * @Param [user, offset, limit]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/13 
    */ 
    Result getExpenseInfo(User user,Integer offset,Integer limit);


    /**
     * 付费服务
     */
    void feeService(User user, BigDecimal price, InvitationDetail.Type type, BigDecimal rate,String meno);

    /**
     * 提现申请
     */
    Result withdraw(User user,String account,String name,BigDecimal amount);

    /**
     * 查找用户排行榜
     */
    Result getRaking(String appType);

    /**
     * 计算提成
     */
    BigDecimal cacluateDeduct(User user,InvitationDetail.Type type,BigDecimal price,BigDecimal rate);

    /**
     *查询金钱
     */
    Result getMoney(User user);
}
