package com.hy.wf.admin.modules.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hy.wf.admin.modules.dao.FunctionRuleDao;
import com.hy.wf.admin.modules.service.FunctionRuleService;
import com.hy.wf.entity.FunctionRule;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-21 18:10
 **/
@Service
public class FunctionRuleServiceImpl extends ServiceImpl<FunctionRuleDao, FunctionRule> implements FunctionRuleService {

    @Override
    public void save(FunctionRule functionRule) {
        this.insert(functionRule);
    }

    @Override
    public void update(FunctionRule functionRule) {
        this.updateById(functionRule);
    }

    @Override
    public List<FunctionRule> findByFunctionIds(List<Long> functionIds) {
        return this.selectList(new EntityWrapper<FunctionRule>().in("function_id",functionIds));
    }

    @Override
    public FunctionRule findByFunctionId(Long functionId) {
        return this.selectOne(new EntityWrapper<FunctionRule>().eq("function_id",functionId));
    }

}
