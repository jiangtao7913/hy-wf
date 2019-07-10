package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.InvitationService;
import com.hy.wf.admin.modules.service.UserFunctionService;
import com.hy.wf.admin.modules.service.UserService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.common.validator.group.UpdateGroup;
import com.hy.wf.entity.User;
import com.hy.wf.entity.UserFunction;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-20 15:44
 **/
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserFunctionService userFunctionService;

    /**
     * app用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("user:save")
    public R save(@RequestBody User user){
        ValidatorUtils.validateEntity(user, AddGroup.class);
        userService.save(user);
        return R.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("user:info")
    public R info(@PathVariable("userId") Long userId){
        User user = userService.selectById(userId);
        return R.ok().put("user", user);
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public R update(@RequestBody User user){
        ValidatorUtils.validateEntity(user, UpdateGroup.class);
        userService.update(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public R delete(@RequestBody Long[] userIds){
        userService.deleteIdentify(userIds);
        return R.ok();
    }


    /**
     * app用户功能列表
     */
    @RequestMapping("/userFunction/list")
    @RequiresPermissions("userFunction:list")
    public R userFunctionList(@RequestParam Map<String, Object> params){
        PageUtils page = userFunctionService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存用户功能
     */
    @SysLog("用户功能")
    @RequestMapping("/userFunction/save")
    @RequiresPermissions("userFunction:save")
    public R userFunctionSave(@RequestBody UserFunction userFunction){
        ValidatorUtils.validateEntity(userFunction, AddGroup.class);
        userFunctionService.save(userFunction);
        return R.ok();
    }

    /**
     * 用户功能信息
     */
    @RequestMapping("/userFunction/info/{id}")
    @RequiresPermissions("userFunction:info")
    public R userFunctionInfo(@PathVariable("id") Long id){
        UserFunction userFunction = userFunctionService.selectById(id);
        return R.ok().put("userFunction", userFunction);
    }

    /**
     * 用户功能
     */
    @SysLog("用户功能")
    @RequestMapping("/userFunction/update")
    @RequiresPermissions("userFunction:update")
    public R userFunctionUpdate(@RequestBody UserFunction userFunction){
        ValidatorUtils.validateEntity(userFunction, UpdateGroup.class);
        userFunctionService.updateById(userFunction);
        return R.ok();
    }

    /**
     * 用户功能
     */
    @SysLog("用户功能")
    @RequestMapping("/userFunction/delete")
    @RequiresPermissions("userFunction:delete")
    public R userFunctionDelete(@RequestBody Long[] ids){
        userFunctionService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

}
