package com.hy.wf.admin.modules.controller;

import com.hy.wf.admin.common.annotation.SysLog;
import com.hy.wf.admin.modules.service.InvitationDetailService;
import com.hy.wf.admin.modules.sys.controller.AbstractController;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.common.util.R;
import com.hy.wf.common.validator.ValidatorUtils;
import com.hy.wf.common.validator.group.AddGroup;
import com.hy.wf.common.validator.group.UpdateGroup;
import com.hy.wf.entity.InvitationDetail;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 15:20
 **/
@RestController
@RequestMapping("/invitationDetail")
public class InvitationDetailController extends AbstractController {

    @Autowired
    private InvitationDetailService invitationDetailService;

    /**
     * 邀请列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("invitationDetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = invitationDetailService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 保存邀请
     */
    @SysLog("保存邀请明细")
    @RequestMapping("/save")
    @RequiresPermissions("invitationDetail:save")
    public R save(@RequestBody InvitationDetail invitationDetail){
        ValidatorUtils.validateEntity(invitationDetail, AddGroup.class);
        invitationDetailService.save(invitationDetail);
        return R.ok();
    }

    /**
     * 邀请信息明细
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("invitationDetail:info")
    public R info(@PathVariable("id") Long id){
        InvitationDetail invitationDetail = invitationDetailService.selectById(id);
        return R.ok().put("invitationDetail", invitationDetail);
    }

    /**
     * 修改邀请
     */
    @SysLog("修改邀请明细")
    @RequestMapping("/update")
    @RequiresPermissions("invitation:update")
    public R update(@RequestBody InvitationDetail invitationDetail){
        ValidatorUtils.validateEntity(invitationDetail, UpdateGroup.class);
        invitationDetailService.update(invitationDetail);
        return R.ok();
    }

    /**
     * 删除邀请明细
     */
    @SysLog("删除邀请明细")
    @RequestMapping("/delete")
    @RequiresPermissions("invitationDetail:delete")
    public R delete(@RequestBody Long[] ids){
        invitationDetailService.delete(ids);
        return R.ok();
    }
}
