package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.common.util.PageUtils;
import com.hy.wf.entity.Ad;
import com.hy.wf.entity.Function;

import java.util.List;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-22 16:20
 **/
public interface AdService extends IService<Ad> {

    PageUtils queryPage(Map<String, Object> params);

    void save(Ad ad);

    void update(Ad ad);

    List<Ad> findByPositionAndAppType(String position,String appType);

    void deleteRedis(Ad ad);

    List<Ad> updateRedis(List<Ad> list,Ad ad);
}
