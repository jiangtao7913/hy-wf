package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.UserFunctionService;
import com.hy.wf.admin.modules.service.UserService;
import com.hy.wf.admin.modules.service.VipService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.common.validator.group.UpdateGroup;
import com.hy.wf.entity.User;
import com.hy.wf.entity.UserFunction;
import com.hy.wf.entity.Vip;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 15:44
 **/
@RestController
@RequestMapping("/vip")
public class VipController extends AbstractController {
    @Autowired
    private VipService vipService;


    /**
     * vip列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("vip:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = vipService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存vip
     */
    @SysLog("保存vip")
    @RequestMapping("/save")
    @RequiresPermissions("vip:save")
    public R save(@RequestBody Vip vip){
        ValidatorUtils.validateEntity(vip, AddGroup.class);
        vipService.save(vip);
        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("vip:info")
    public R info(@PathVariable("id") Long id){
        Vip vip = vipService.selectById(id);
        return R.ok().put("vip", vip);
    }

    /**
     * 修改vip
     */
    @SysLog("修改vip")
    @RequestMapping("/update")
    @RequiresPermissions("vip:update")
    public R update(@RequestBody Vip vip){
        ValidatorUtils.validateEntity(vip, UpdateGroup.class);
        vipService.updateById(vip);
        return R.ok();
    }

    /**
     * 删除vip
     */
    @SysLog("删除vip")
    @RequestMapping("/delete")
    @RequiresPermissions("vip:delete")
    public R delete(@RequestBody Long[] ids){
        vipService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }



}
