package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.modules.service.ChannelRecordService;
import com.hy.wf.admin.modules.service.OrderService;
import com.hy.wf.admin.modules.service.RecordService;
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
 * @create: 2019-03-26 11:42
 **/
@RestController
@RequestMapping("/statistics")
public class StatisticsController extends AbstractController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private ChannelRecordService channelRecordService;

    /**
     * 查看消费统计列表
     */
    @RequestMapping("/pay/list")
    @RequiresPermissions("pay:list")
    public R payList(@RequestParam Map<String, Object> params){
        PageUtils page = recordService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 查看消费统计列表
     */
    @RequestMapping("/channel/list")
    @RequiresPermissions("channel:list")
    public R channelList(@RequestParam Map<String, Object> params){
        PageUtils page = channelRecordService.queryPage(params);
        return R.ok().put("page", page);
    }


}
