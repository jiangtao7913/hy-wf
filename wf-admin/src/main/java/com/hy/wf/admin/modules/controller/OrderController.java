package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.modules.service.MoneyRecordService;
import com.hy.wf.admin.modules.service.OrderService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-25 16:04
 **/
@RestController
@RequestMapping("/order")
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MoneyRecordService moneyRecordService;

    /**
     * 查看订单列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("order:list")
    public R orderList(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查看明细列表
     */
    @RequestMapping("/moneyRecord/list")
    @RequiresPermissions("order:list")
    public R moneyDetailList(@RequestParam Map<String, Object> params){
        PageUtils page = moneyRecordService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 审核
     */
    @RequestMapping("/withdrawAudit")
    @RequiresPermissions("order:withdrawAudit")
    public R withdrawAudit(Long id,Integer status,String reason){
        moneyRecordService.withdrawAudit(id,status,reason);
        return R.ok();
    }
}
