package com.hy.wf.admin.modules.service;

import com.baomidou.mybatisplus.service.IService;
import com.hy.wf.entity.FunctionRule;
import com.hy.wf.entity.Invitation;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 18:09
 **/
public interface FunctionRuleService  extends IService<FunctionRule> {

    void save(FunctionRule functionRule);

    void update(FunctionRule functionRule);

    List<FunctionRule> findByFunctionIds(List<Long> functionIds);

    FunctionRule findByFunctionId(Long functionId);

}
