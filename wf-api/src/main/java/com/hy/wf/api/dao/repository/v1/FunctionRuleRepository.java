package com.hy.wf.api.dao.repository.v1;

import com.hy.wf.api.dao.base.BaseRepository;
import com.hy.wf.entity.FunctionRule;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-08 12:41
 **/
public interface FunctionRuleRepository extends BaseRepository<FunctionRule,Long> {

    List<FunctionRule> findInFunctionIds(List<Long> functionIds);
}
