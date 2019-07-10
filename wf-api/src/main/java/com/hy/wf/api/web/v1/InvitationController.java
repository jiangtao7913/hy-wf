package com.hy.wf.api.web.v1;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.service.v1.InvitationService;
import com.hy.wf.api.web.BaseController;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.entity.InvitationDetail;
import com.hy.wf.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.FormParam;
import java.math.BigDecimal;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-06 16:08
 **/
@RestController
@RequestMapping("/invite")
@Slf4j
public class InvitationController extends BaseController {

    @Autowired
    private InvitationService invitationService;

    /**
     * 邀请页面信息
     */
    @PostMapping("/getInvitationInfo")
    @CustomLog
    public Result getInvitationInfo(@RequestHeader("appType")String appType){
        User user = getCurrentUser();
        return invitationService.findInvitationInfo(user,appType);
    }

    /**
     * 填写邀请码
     */
    @PostMapping("/{invitationCode}")
    @CustomLog
    public Result fillCode(@PathVariable("invitationCode")String invitationCode,@RequestHeader("appType")String appType){
        return invitationService.fillCode(getCurrentUser(),invitationCode);
    }

    /**
     * 查看粉丝
     */
    @PostMapping("/getFansInfo/{type}/{offset}/{limit}")
    @CustomLog
    public Result getFansInfo(@PathVariable("type")String type,@PathVariable("offset")Integer offset,@PathVariable("limit")Integer limit,
                              @RequestHeader("appType")String appType){
        InvitationDetail.Type mType = InvitationDetail.Type.valueOf(type);
        if(null == mType){
            throw new ServiceException(ErrorCode.C400);
        }
        return invitationService.getFansInfo(getCurrentUser(),mType ,appType,offset,limit);
    }

    /**
     * 查看收益记录
     */
    @PostMapping("/getIncomeInfo/{offset}/{limit}")
    @CustomLog
    public Result getIncomeInfo(@PathVariable("offset")Integer offset,@PathVariable("limit")Integer limit,@RequestHeader("appType")String appType){
        return invitationService.getIncomeInfo(getCurrentUser(),offset,limit);
    }


    /**
     * 查看支出记录
     */
    @PostMapping("/getExpenseInfo/{offset}/{limit}")
    @CustomLog
    public Result getExpenseInfo(@PathVariable("offset")Integer offset,@PathVariable("limit")Integer limit,@RequestHeader("appType")String appType){
        return invitationService.getExpenseInfo(getCurrentUser(),offset,limit);
    }

    /**
     * 申请提现接口
     */
    @PostMapping("/withdraw")
    @CustomLog
    public Result withdraw(@FormParam("account") String account, @FormParam("name")String name, @FormParam("amount")BigDecimal amount){
        return invitationService.withdraw(getCurrentUser(),account,name,amount);
    }

    /**
     * 查询用户排行榜
     */
    @PostMapping("/getRaking")
    @CustomLog
    public Result getRaking(@RequestHeader("appType")String appType){
        return invitationService.getRaking(appType);
    }

    /**
     * 查询用户累计收益和余额
     */
    @PostMapping("/getMoney")
    @CustomLog
    public Result getMoney(){
        User user = getCurrentUser();
        return invitationService.getMoney(user);
    }

}
