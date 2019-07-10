package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.FunctionDao;
import com.hy.wf.admin.modules.service.CommonService;
import com.hy.wf.admin.modules.service.FunctionService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.User;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 15:43
 **/
@Service
public class FunctionServiceImpl extends ServiceImpl<FunctionDao, Function> implements FunctionService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");

        Page<Function> page = this.selectPage(
                new Query<Function>(params).getPage(),
                new EntityWrapper<Function>()
                        //.eq("data_status", BaseEntity.DataStatus.valid.value)
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()),Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    /**
     * 保存功能，同步redis数据
     * @param function
     */
    @Override
    public void save(Function function) {
        function.init();
        this.insert(function);
    }

    /**
     * 修改功能同步redis数据
     * @param function
     */
    @Override
    public void update(Function function) {
        this.updateById(function);
    }

    @Override
    public List<Function> findByPositionAndAppType(String position,String appType) {
        return selectList(
                new EntityWrapper<Function>()
                        .eq("position",position)
                        .eq("app_type",appType)
                        .eq("data_status",Function.DataStatus.valid.value)
                        .orderAsc(Arrays.asList("status","orders"))
        );
    }

    @Override
    @CacheEvict(value="function",key = "#function.getAppType()+'-'+#function.getPosition()")
    public void deleteRedis(Function function) {

    }

    @Override
    @CachePut(value = "function",key = "#function.getAppType()+'-'+#function.getPosition()")
    public List<CommonService.OutPut> updateRedis(List<CommonService.OutPut> outPutList,Function function) {
        return outPutList;
    }


}
