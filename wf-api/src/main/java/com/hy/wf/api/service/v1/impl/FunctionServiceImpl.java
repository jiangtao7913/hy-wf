package com.hy.wf.api.service.v1.impl;
import com.hy.wf.entity.FunctionRule;
import com.hy.wf.entity.Function;
import java.util.*;

import com.hy.wf.api.dao.repository.v1.*;
import com.hy.wf.api.service.v1.FunctionService;
import com.hy.wf.api.service.v1.UserFunctionService;
import com.hy.wf.common.Result;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.entity.*;
import com.hy.wf.entity.base.BaseEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-02 15:13
 **/
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private UserFunctionRepository userFunctionRepository;
    @Autowired
    private FunctionUseRepository functionUseRepository;
    @Autowired
    private FunctionRuleRepository functionRuleRepository;

    @Override
    @Cacheable(value = "function",key = "#appType+'-'+#position")
    public List<OutPut> getAllAndAppType(String position,String appType) {
        List<OutPut> outPutList = new ArrayList<>();
        List<Function> list = functionRepository.findByPositionAndAppType(position,appType);
        if(list.isEmpty()){
            return outPutList;
        }
        List<Long> ids = list.stream().map(Function::getId).collect(Collectors.toList());
        if(ids.isEmpty()){
            return outPutList;
        }
        List<FunctionRule> functionRuleList = functionRuleRepository.findInFunctionIds(ids);
        if(ids.isEmpty()){
            return outPutList;
        }
        Map<Long,FunctionRule> map = functionRuleList.stream().collect(Collectors.toMap(FunctionRule::getFunctionId, java.util.function.Function.identity()));

        for(Function function : list){
            OutPut outPut = new OutPut();
            outPut.setFunction(function);
            outPut.setFunctionRule(map.get(function.getId()));
            outPutList.add(outPut);
        }
        return outPutList;
    }



    @Override
    public Function findById(Long id) {
        return functionRepository.findById(id);
    }

    @Override
    public List<Function> findByType(Function.Type type,String appType) {
        return functionRepository.findByType(type,appType);
    }


    @Override
    public Result functionRecord(Long userId, Long functionId,String hardwareKey) {
        boolean freeFlag = false;
        if(userId !=0){
            UserFunction userFunction = userFunctionRepository.findByUserIdAndFunctionId(functionId,userId);
            if(null != userFunction){
                freeFlag = true;
            }
        }
        FunctionUse.Type type = null;
        if(freeFlag){
            type = FunctionUse.Type.ZF;
        }else {
            type = FunctionUse.Type.TY;
        }
        FunctionUse functionUse = functionUseRepository.findByHardwareKeyAndType(hardwareKey,type);
        if(null == functionUse ){
            functionUse = new FunctionUse();
            functionUse.setFunctionId(functionId);
            functionUse.setHardwareKey(hardwareKey);
            functionUse.setUseCount(1);
            functionUse.setUserId(userId);
            functionUse.setType(type.value);
            functionUse.init();
            functionUseRepository.save(functionUse);
        }else{
            functionUse.setUseCount(functionUse.getUseCount()+1);
            if(!freeFlag){
                if(functionUse.getUseCount()>3){
                    throw new ServiceException(ErrorCode.C6001);
                }
            }

            functionUseRepository.updateUserCountById(functionUse.getId(),functionUse.getUseCount());
        }
        Map<String,Object> map = new HashMap<>(1);
        map.put("result",functionUse.getUseCount());
        return Result.success(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result checkVip(Long userId) {
        long time = System.currentTimeMillis();
        List<UserFunction> userFunctionList = userFunctionRepository.findByUserId(userId);
        Map<String,Object> map = new HashMap<>(1);
        if(null == userFunctionList){
            map.put("result",true);
            return Result.success(map);
        }
        List<UserFunction> userFunctionListExpireTime = new ArrayList<>();
        List<UserFunction> userFunctionListNotExpireTime = new ArrayList<>();
        for(UserFunction userFunction : userFunctionList){
           long expireTime = userFunction.getExpireTime().getTime();
           if(expireTime<time){
               userFunction.setDataStatus(BaseEntity.DataStatus.invalid.value);
               userFunctionListExpireTime.add(userFunction);
           }else{
               userFunctionListNotExpireTime.add(userFunction);
           }
        }
        userFunctionRepository.batchUpdate(userFunctionListExpireTime);
        return Result.success(userFunctionListNotExpireTime);
    }

}
