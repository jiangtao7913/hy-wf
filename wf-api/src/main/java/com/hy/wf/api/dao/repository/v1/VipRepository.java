package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Vip;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-17 18:19
 **/
public interface VipRepository extends BaseRepository<Vip,Long> {

    List<Vip> findList(String appType);
}
