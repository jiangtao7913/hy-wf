package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.InvitationDetailRepository;
import com.hy.wf.api.dao.repository.v1.InvitationRepository;
import com.hy.wf.api.dao.repository.v1.MoneyRecordRepository;
import com.hy.wf.api.dao.repository.v1.UserRepository;
import com.hy.wf.api.service.v1.AppConfigService;
import com.hy.wf.api.service.v1.InvitationService;
import com.hy.wf.api.service.v1.SnService;
import com.hy.wf.common.Constant;
import com.hy.wf.common.Result;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.entity.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-06 16:11
 **/
@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private InvitationDetailRepository invitationDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private MoneyRecordRepository moneyRecordRepository;
    @Autowired
    private SnService snService;



    @Override
    public Result findInvitationInfo(User user,String appType) {
        //查询邀请信息
        Invitation invitation = invitationRepository.findByMasterId(user.getId());
        //查询提成信息
        String appConfig = appConfigService.getValue("invitation","invitation_config",appType);
        boolean flag = false;
        //查询是否有一级师傅
        InvitationDetail invitationDetail =  invitationDetailRepository.finByPreticeUidAndType(user.getUid(),InvitationDetail.Type.One);
        if(null == invitationDetail){
            flag = true;
        }
        Map<String, Object> map = new HashMap<>(4);
        map.put("invitation",invitation);
        map.put("invitationConfig",appConfig);
        map.put("balance",user.getBalance());
        map.put("flag",flag);
        return Result.success(map);
    }

    /** 
    * @Description: 填写邀请码
    * @Param [user, code]
    * @return: com.hy.wf.common.Result 
    * @Author: jt 
    * @Date: 2019/3/12 
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result fillCode(User user, String code) {
        //查找师傅的邀请信息
        Invitation invitation = invitationRepository.findByMasterUid(code);
        //查找师傅个人的信息
        User masterUser = userRepository.findByUid(code);

        //邀请码不存在
        checkNotExistsCode(masterUser);
        //不能自己邀请自己
        checkOwner(user,code);
        //不能互相邀请
        checkEach(user,masterUser);



        if(null == invitation){
            invitation = createInvitation(masterUser);
        }else{
            int count = invitation.getCount()+1;
            invitationRepository.updateCountById(count,invitation.getId());
        }
        //不能重复邀请
        checkRepeat(invitation,user);

        //查看师傅是否有师傅
        InvitationDetail invitationDetail = invitationDetailRepository.finByPreticeUidAndType(code, InvitationDetail.Type.One);
        if(null != invitationDetail){
            //生成二级徒弟明细
            createInvitationDetail(user,invitationDetail.getInvitationId(),InvitationDetail.Type.Two);
            Invitation invitationMaster = invitationRepository.findById(invitationDetail.getInvitationId());
            //增加邀请数量
            int count = invitationMaster.getCount()+1;
            invitationRepository.updateCountById(count,invitationDetail.getInvitationId());
        }
        //生成一级徒弟信息
        createInvitationDetail(user,invitation.getId(),InvitationDetail.Type.One);
        Map<String,Object> map = new HashMap<>();
        map.put("result",true);
        return Result.success(map);
    }

    @Override
    public Result getFansInfo(User user, InvitationDetail.Type type,String appType,Integer offset,Integer limit) {
        List<InvitationDetail> list = new ArrayList<>();
        Invitation invitation = invitationRepository.findByMasterId(user.getId());
        if(null != invitation){
            list = invitationDetailRepository.findByInvitationIdAndType(invitation.getId(),type,offset,limit);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("fans",list);
        //查询提成信息
        String appConfig = appConfigService.getValue("invitation","invitation_config",appType);
        String divide = null;
        if(null != appConfig){
           String[] str = appConfig.split(",");
           if(type.equals(InvitationDetail.Type.One)){
               divide = str[0];
           }else{
               divide = str[1];
           }
        }
        map.put("divide",divide);
        return Result.success(map);
    }

    @Override
    public Result getIncomeInfo(User user,Integer offset,Integer limit) {
        List<MoneyRecord> moneyRecordList = moneyRecordRepository.findByUserIdAndType(user.getId(),offset,limit, MoneyRecord.Type.Income);
        return Result.success(moneyRecordList);
    }

    @Override
    public Result getExpenseInfo(User user, Integer offset, Integer limit) {
        List<MoneyRecord> moneyRecordList =  moneyRecordRepository.findByUserIdAndNotType(user.getId(),offset,limit, MoneyRecord.Type.Income);
        return Result.success(moneyRecordList);
    }

    @Override
    public BigDecimal cacluateDeduct(User user,InvitationDetail.Type type,BigDecimal price,BigDecimal rate){
        //查看当前付费用户是否有师傅
        InvitationDetail invitationDetail = invitationDetailRepository.finByPreticeUidAndType(user.getUid(), type);
        if(null != invitationDetail){
            Invitation invitation = invitationRepository.findById(invitationDetail.getInvitationId());
            if(null != invitation){
                BigDecimal addMoney = price.multiply(rate.divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_CEILING);
                return addMoney;
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Result getMoney(User user) {
        Map<String,Object> map = new HashMap<>();
        Invitation invitation = invitationRepository.findByMasterId(user.getId());
        map.put("incomeTotal",invitation==null?BigDecimal.ZERO : invitation.getIncomeTotal());
        map.put("balance",user.getBalance());
        return Result.success(map);
    }

    @Override
    public void feeService(User user,BigDecimal price,InvitationDetail.Type type,BigDecimal rate,String meno) {
       //生成支出记录表,只执行一次
       if(type.value ==InvitationDetail.Type.One.value){
           createMoneyRecord("","",user,meno,user,MoneyRecord.Type.Consume,price,MoneyRecord.Status.Successful);
       }
       //查看当前付费用户是否有师傅
       InvitationDetail invitationDetail = invitationDetailRepository.finByPreticeUidAndTypeForUpdate(user.getUid(), type);
       if(null != invitationDetail){
           //给师傅提成百分之
           Invitation invitation = invitationRepository.findByIdForUpdate(invitationDetail.getInvitationId());
           if(null != invitation){
               User userMaster = userRepository.findByIdForUpdate(invitation.getMasterId());
               BigDecimal addMoney = price.multiply(rate.divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_DOWN);
               BigDecimal balance = userMaster.getBalance().add(addMoney);
               userRepository.updateBalanceById(userMaster.getId(),balance);
               //修改邀请收益记录
               BigDecimal incomeTotal = invitation.getIncomeTotal().add(addMoney);
               BigDecimal stairIncome = invitation.getStairIncome();
               BigDecimal secondIncome = invitation.getSecondIncome();
               if(type.equals(InvitationDetail.Type.One)){
                   stairIncome = stairIncome.add(addMoney);
               }else {
                   secondIncome = secondIncome.add(addMoney);
               }
               invitationRepository.updateIncomeById(invitation.getId(),incomeTotal,stairIncome,secondIncome);
               BigDecimal detailIncomeTotal = invitationDetail.getIncomeTotal().add(addMoney);
               invitationDetailRepository.updateIncomeTotalById(invitationDetail.getId(),detailIncomeTotal);
               //生成收益记录
               createMoneyRecord("","",userMaster,meno,user,MoneyRecord.Type.Income,addMoney,MoneyRecord.Status.Successful);
           }
       }
    }

    /**
     * 生成收益记录
     */
    private void createMoneyRecord(String account,String name,User userMaster, String meno, User user, MoneyRecord.Type type,BigDecimal addMoney,MoneyRecord.Status status){
        MoneyRecord moneyRecord = new MoneyRecord();
        moneyRecord.setAccount(account);
        moneyRecord.setName(name);
        moneyRecord.setStatus(status.value);
        moneyRecord.setUserId(userMaster.getId());
        moneyRecord.setMeno(meno);
        moneyRecord.setType(type.value);
        moneyRecord.setNumber(snService.generate(Sn.Type.money));
        moneyRecord.setReason("");
        moneyRecord.setOtherUserName(user.getName());
        moneyRecord.setOtherUserId(user.getId());
        moneyRecord.setAmount(addMoney);
        moneyRecord.init();
        moneyRecordRepository.save(moneyRecord);
    }

    @Override
    public Result withdraw(User user, String account, String name, BigDecimal amount) {
        Map<String,Object> map = new HashMap<>();
        if(amount.compareTo(new BigDecimal(Constant.BALANCE))<0){
            throw new ServiceException(ErrorCode.C7005);
        }
        if(user.getBalance().compareTo(amount)<0){
            throw new ServiceException(ErrorCode.C7004);
        }
        //生成体现记录
        createMoneyRecord(account,name,user,"提现",user,MoneyRecord.Type.Withdraw,amount,MoneyRecord.Status.Audit);
        //扣除用户余额
        BigDecimal balance = user.getBalance().subtract(amount);
        userRepository.updateInfo(user.getId(),"balance",String.valueOf(balance));
        map.put("result",true);
        return Result.success(map);
    }

    @Override
    public Result getRaking(String appType) {
        List<OutPut> outPuts = new ArrayList<>();
        List<Invitation> invitationList = invitationRepository.findOrderIncomeTotal(Constant.LIMIT);
        if(!invitationList.isEmpty()){
            List<Long> userIds = invitationList.stream().map(Invitation::getMasterId).collect(Collectors.toList());
            List<User> userList = userRepository.findInIds(userIds);
            if(!userList.isEmpty()){
                Map<Long,User> map = userList.stream().collect(Collectors.toMap(User::getId, Function.identity()));
                for(Invitation invitation :invitationList){
                    OutPut outPut = new OutPut();
                    outPut.setIcon(map.get(invitation.getMasterId()).getIcon());
                    outPut.setName(map.get(invitation.getMasterId()).getName());
                    outPut.setCount(invitation.getCount());
                    outPut.setIncomeTotal(invitation.getIncomeTotal());
                    outPuts.add(outPut);
                }
            }
        }
        return Result.success(outPuts);
    }

    @Data
    class OutPut {
        private String icon;
        private String name;
        private int count;
        private BigDecimal incomeTotal;
    }

    private void checkNotExistsCode(User user){
        if(null == user){
            log.warn("===========> 邀请码不存在");
            throw new ServiceException(ErrorCode.C7006);
        }
    }

    private void checkRepeat(Invitation invitation,User user){
        InvitationDetail invitationDetailExists = invitationDetailRepository.finByPreticeUidAndType(user.getUid(),InvitationDetail.Type.One);
        if(null != invitationDetailExists){
            log.warn("===========> 不能重复邀请");
            throw new ServiceException(ErrorCode.C7002);
        }
    }
    private void checkEach(User user,User masterUser){
        Invitation invitationPretice = invitationRepository.findByMasterUid(user.getUid());
        if(null != invitationPretice){
            InvitationDetail invitationDetail = invitationDetailRepository.findByInvitationIdAndPreticeId(invitationPretice.getId(),masterUser.getId());
            if(null != invitationDetail){
                log.warn("===========> 不能互相邀请");
                throw new ServiceException(ErrorCode.C7003);
            }
        }
    }
    private void checkOwner(User user,String code){
        if(user.getUid().equals(code)){
            log.warn("===========> 不能自己邀请自己");
            throw new ServiceException(ErrorCode.C7001);
        }
    }

    /**
     * 生成邀请信息
     */
    private Invitation createInvitation(User user){
        Invitation invitation = new Invitation();
        invitation.setMasterId(user.getId());
        invitation.setMasterUid(user.getUid());
        invitation.setIncomeTotal(BigDecimal.ZERO);
        invitation.setStairIncome(BigDecimal.ZERO);
        invitation.setSecondIncome(BigDecimal.ZERO);
        invitation.setCount(1);
        invitation.init();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        invitationRepository.save(invitation,keyHolder);
        invitation.setId(keyHolder.getKey().longValue());
        return invitation;
    }

    /**
     * 生成徒弟明细信息
     * @param user
     * @param invitationId
     * @param type
     */
    private void createInvitationDetail(User user,Long invitationId,InvitationDetail.Type type){
        InvitationDetail invitationDetail = new InvitationDetail();
        invitationDetail.setInvitationId(invitationId);
        invitationDetail.setPreticeId(user.getId());
        invitationDetail.setPreticeUid(user.getUid());
        invitationDetail.setName(user.getName());
        invitationDetail.setMeno("");
        invitationDetail.setIncomeTotal(BigDecimal.ZERO);
        invitationDetail.setType(type.value);
        invitationDetail.init();
        invitationDetailRepository.save(invitationDetail);
    }
}
