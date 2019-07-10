package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.UserFunctionDao;
import com.hy.wf.admin.modules.service.UserFunctionService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.User;
import com.hy.wf.entity.UserFunction;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 17:58
 **/
@Service
public class UserFunctionServiceImpl extends ServiceImpl<UserFunctionDao, UserFunction> implements UserFunctionService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String userId = (String)params.get("userId");

        Page<UserFunction> page = this.selectPage(
                new Query<UserFunction>(params).getPage(),
                new EntityWrapper<UserFunction>()
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(userId),"user_id", userId)
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );

        return new PageUtils(page);
    }

    @Override
    public void save(UserFunction userFunction) {
        userFunction.init();
        this.insert(userFunction);
    }
}
