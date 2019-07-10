package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.PluginService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hy.wf.admin.modules.dao.entity.PluginConfig;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 14:57
 **/
@RestController
@RequestMapping("/plugin")
public class PluginController extends AbstractController {

    @Autowired
    private PluginService pluginService;


    /**
     * 查看插件列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("plugin:list")
    public R functionList(@RequestParam Map<String, Object> params){
        PageUtils page = pluginService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存插件
     */
    @SysLog("保存插件")
    @RequestMapping("/save")
    @RequiresPermissions("plugin:save")
    public R functionSave(@RequestBody PluginConfig pluginConfig){
        ValidatorUtils.validateEntity(pluginConfig, AddGroup.class);
        pluginService.save(pluginConfig);
        return R.ok();
    }

    /**
     * 修改插件
     */
    @SysLog("保存插件")
    @RequestMapping("/update")
    @RequiresPermissions("plugin:update")
    public R functionUpdate(@RequestBody PluginConfig pluginConfig){
        ValidatorUtils.validateEntity(pluginConfig, AddGroup.class);
        pluginService.update(pluginConfig);
        return R.ok();
    }

    /**
     * 插件信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("plugin:info")
    public R functionInfo(@PathVariable("id") Long id){
        PluginConfig pluginConfig = pluginService.selectById(id);
        return R.ok().put("pluginConfig", pluginConfig);
    }

    /**
     * 删除插件
     *
     */
    @SysLog("删除插件")
    @RequestMapping("/delete")
    @RequiresPermissions("plugin:delete")
    public R deleteFunction(@RequestBody Long[] ids){
        pluginService.delete(ids);
        return R.ok();
    }
}
