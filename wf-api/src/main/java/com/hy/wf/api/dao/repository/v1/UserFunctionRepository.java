package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.UserFunction;

import java.util.Date;
import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-04 18:56
 **/
public interface UserFunctionRepository extends BaseRepository<UserFunction,Long> {

    List<UserFunction> findByUserId(Long userId);

    UserFunction findByUserIdAndTypeAndId(Long functionId,Long userId,UserFunction.Type type);

    int updateExpireTimeById(Long id, Date expireTime);


    List<UserFunction> findByUserIdAndType(Long userId, UserFunction.Type type);

    UserFunction findByUserIdAndFunctionId(Long functionId,Long userId);

    UserFunction findByUserIdAndVip(Long userId, UserFunction.Type type);
}
