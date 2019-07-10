package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.InvitationService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.common.validator.group.UpdateGroup;
import com.hy.wf.entity.Invitation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 13:01
 **/
@RestController
@RequestMapping("/invitation")
public class InvitationController extends AbstractController {

    @Autowired
    private InvitationService invitationService;

    /**
     * 邀请列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("invitation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = invitationService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存邀请
     */
    @SysLog("保存邀请")
    @RequestMapping("/save")
    @RequiresPermissions("invitation:save")
    public R save(@RequestBody Invitation invitation){
        ValidatorUtils.validateEntity(invitation, AddGroup.class);
        invitationService.save(invitation);
        return R.ok();
    }

    /**
     * 邀请信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("invitation:info")
    public R info(@PathVariable("id") Long id){
        Invitation invitation = invitationService.selectById(id);
        return R.ok().put("invitation", invitation);
    }

    /**
     * 修改邀请
     */
    @SysLog("修改邀请")
    @RequestMapping("/update")
    @RequiresPermissions("invitation:update")
    public R update(@RequestBody Invitation invitation){
        ValidatorUtils.validateEntity(invitation, UpdateGroup.class);
        invitationService.updateById(invitation);
        return R.ok();
    }

    /**
     * 删除邀请
     */
    @SysLog("删除邀请")
    @RequestMapping("/delete")
    @RequiresPermissions("invitation:delete")
    public R delete(@RequestBody Long[] ids){
        invitationService.delete(ids);
        return R.ok();
    }


}
