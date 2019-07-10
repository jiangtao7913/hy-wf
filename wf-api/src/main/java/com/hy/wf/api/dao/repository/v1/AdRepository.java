package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Ad;

import java.util.Date;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-09 10:58
 **/
public interface AdRepository extends BaseRepository<Ad,Long> {

    /**
    * @Description: 根据position,startDate,endDate查询广告
    * @Param [position, startDate, endDate]
    * @return: java.util.List<com.hy.wf.dao.entity.v1.Ad>
    * @Author: jt
    * @Date: 2019/1/9
    */
    List<Ad> findByPositionBetweenStartDateAndEndDateAndAppType(String position, Date startDate, Date endDate,String appType);
}
