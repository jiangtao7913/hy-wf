package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.Function;
import com.hy.wf.entity.InstallLog;
import com.hy.wf.entity.UserFunction;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-02 15:23
 **/
public interface FunctionRepository extends BaseRepository<Function,Long> {

    /**
     * 根据位置查询功能
     * @param position
     * @return
     */
    List<Function> findByPositionAndAppType(String position,String appType);

    /**
     * 根据类型查询功能
     * @param type
     * @return
     */
    List<Function> findByType(Function.Type type,String appType);

    List<Function> findByNotInIdsAndType(List<Long> ids,List<Integer> types,String appType);

}
