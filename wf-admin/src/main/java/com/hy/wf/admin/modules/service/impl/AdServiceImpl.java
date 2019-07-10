package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.common.utils.Constant;
import com.hy.wf.admin.common.utils.Query;
import com.hy.wf.admin.modules.dao.AdDao;
import com.hy.wf.admin.modules.service.AdService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.base.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-22 16:21
 **/
@Service
public class AdServiceImpl  extends ServiceImpl<AdDao, Ad> implements AdService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String title = (String)params.get("title");

        Page<Ad> page = this.selectPage(
                new Query<Ad>(params).getPage(),
                new EntityWrapper<Ad>()
                        .like(StringUtils.isNotBlank(title),"title", title)
                        .eq("data_status", BaseEntity.DataStatus.valid.value)
                        .orderBy(!StringUtils.isNotBlank(params.get("sidx").toString()), Arrays.asList("id"),false)
                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public void save(Ad ad) {
        ad.init();
        ad.setBeginDate(new Date(1l));
        ad.setEndDate(new Date(1922352010000l));
        this.insert(ad);
    }

    @Override
    public void update(Ad ad) {
        this.updateById(ad);
    }

    @Override
    public List<Ad> findByPositionAndAppType(String position,String appType) {
        return selectList(
                new EntityWrapper<Ad>()
                        .eq("position",position)
                        .eq("app_type",appType)
                        .eq("data_status",Ad.DataStatus.valid.value)
        );
    }

    @Override
    @CacheEvict(value="ad",key = "#ad.getAppType()+'-'+#ad.getPosition()")
    public void deleteRedis(Ad ad) {

    }

    @Override
    @CachePut(value = "ad",key = "#ad.getAppType()+'-'+#ad.getPosition()")
    public List<Ad> updateRedis(List<Ad> list, Ad ad) {
        return list;
    }

}
