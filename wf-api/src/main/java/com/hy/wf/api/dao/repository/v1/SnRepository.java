package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Sn;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-18 16:38
 **/
public interface SnRepository extends BaseRepository<Sn,Long> {

    Sn findByType(Sn.Type type);

    int updateById(Sn sn);
}
