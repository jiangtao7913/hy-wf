package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.PluginAttributeService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.entity.PluginConfigAttribute;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-26 14:57
 **/
@RestController
@RequestMapping("/pluginAttribute")
public class PluginAttributeController extends AbstractController {

    @Autowired
    private PluginAttributeService pluginAttributeService;
  
    
    /**
     * 查看插件列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("plugin_attribute:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = pluginAttributeService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存插件
     */
    @SysLog("保存插件")
    @RequestMapping("/save")
    @RequiresPermissions("plugin_attribute:save")
    public R save(@RequestBody PluginConfigAttribute pluginConfigAttribute){
        ValidatorUtils.validateEntity(pluginConfigAttribute, AddGroup.class);
        pluginAttributeService.save(pluginConfigAttribute);
        return R.ok();
    }

    /**
     * 修改插件
     */
    @SysLog("保存插件")
    @RequestMapping("/update")
    @RequiresPermissions("plugin_attribute:update")
    public R update(@RequestBody PluginConfigAttribute pluginConfigAttribute){
        ValidatorUtils.validateEntity(pluginConfigAttribute, AddGroup.class);
        pluginAttributeService.update(pluginConfigAttribute);
        return R.ok();
    }

    /**
     * 插件信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("plugin_attribute:info")
    public R info(@PathVariable("id") Long id){
        PluginConfigAttribute pluginConfigAttribute = pluginAttributeService.selectById(id);
        return R.ok().put("pluginConfigAttribute", pluginConfigAttribute);
    }

    /**
     * 删除插件
     *
     */
    @SysLog("删除插件")
    @RequestMapping("/delete")
    @RequiresPermissions("plugin_attribute:delete")
    public R delete(@RequestBody Long[] ids){
        pluginAttributeService.delete(ids);
        return R.ok();
    }

    /**
     * 插件信息
     */
    @RequestMapping("/getPluginName")
    @RequiresPermissions("plugin_attribute:info")
    public R getPluginName(){
        List<Map<String,String>> mapList =  pluginAttributeService.getPluginName();
        return R.ok().put("mapList",mapList);
    }

}
