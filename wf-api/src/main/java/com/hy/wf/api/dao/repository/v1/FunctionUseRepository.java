package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.FunctionUse;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:43
 **/
public interface FunctionUseRepository extends BaseRepository<FunctionUse,Long> {

    FunctionUse findByHardwareKeyAndType(String hardwareKey, FunctionUse.Type type);

    int updateUserCountById(Long id,int userCount);
}
