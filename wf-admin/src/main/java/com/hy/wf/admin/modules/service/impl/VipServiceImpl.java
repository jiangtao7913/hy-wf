package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.VipDao;
import com.hy.wf.admin.modules.service.VipService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.User;
import com.hy.wf.entity.Vip;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 19:39
 **/
@Service
public class VipServiceImpl extends ServiceImpl<VipDao, Vip> implements VipService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String type = (String)params.get("type");

        Page<Vip> page = this.selectPage(
                new Query<Vip>(params).getPage(),
                new EntityWrapper<Vip>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(type),"name", type)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void save(Vip vip) {
        vip.init();
        this.insert(vip);
    }

}
