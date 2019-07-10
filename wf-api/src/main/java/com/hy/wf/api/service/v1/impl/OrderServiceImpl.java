package com.hy.wf.api.service.v1.impl;

import com.hy.wf.api.dao.repository.v1.FunctionRepository;
import com.hy.wf.api.dao.repository.v1.OrderRepository;
import com.hy.wf.api.dao.repository.v1.UserFunctionRepository;
import com.hy.wf.api.dao.repository.v1.UserRepository;
import com.hy.wf.api.service.pay.PaymentPlugin;
import com.hy.wf.api.service.pay.PluginService;
import com.hy.wf.api.service.v1.*;
import com.hy.wf.common.Constant;
import com.hy.wf.common.DateUtils;
import com.hy.wf.common.Result;
import com.hy.wf.common.exception.ErrorCode;
import com.hy.wf.common.exception.ServiceException;
import com.hy.wf.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-01-16 17:56
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PluginService pluginService;
    @Autowired
    private VipService vipService;
    @Autowired
    private SnService snService;
    @Autowired
    private FunctionService functionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFunctionRepository userFunctionRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private UserFunctionService userFunctionService;

    @Autowired
    private RecordService recordService;
    @Autowired
    private ChannelRecordService channelRecordService;


    /**
     * 根据订单编号查询订单
     *
     * @param orderNumber
     * @return
     */
    @Override
    public Order findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }


    /**
     * 生成未支付订单接口
     *
     * @param type
     * @param pluginName
     * @param id
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result produceOrder(Order.Type type, String pluginName, Long id, User user, HttpServletRequest request,String appType) {
        //1.判断插件名称是否正确
        PaymentPlugin paymentPlugin = pluginService.getPaymentPlugin(pluginName);

        String description = null;
        BigDecimal price = BigDecimal.ZERO;
        Long businessId = 0L;
        //支付宝，微信商品名称
        String name = null;
        if (null == paymentPlugin) {
            log.error("pluginName 错误");
            return Result.fail(ErrorCode.C400);
        }

        if(type.value == Order.Type.Single.value){
            //购买单个功能
            Function function = functionService.findById(id);
            if(null == function ){
                log.error("id 错误");
                return Result.fail(ErrorCode.C400);
            }
            description =  user.getName()+"用户开通了"+ function.getTitle() +"功能";
            price = function.getPrice();
            businessId = function.getId();
            name = function.getTitle();
        }else if(type.value == Order.Type.Vip.value){
            //购买vip
            Vip vip = vipService.findVipById(id);
            if (null == vip) {
                log.error("id 错误");
                return Result.fail(ErrorCode.C400);
            }
            description =  user.getName()+"用户购买了"+Vip.Type.valueOf(vip.getType()).remark;
            price = vip.getPrice();
            businessId = vip.getId();
            name = Vip.Type.valueOf(vip.getType()).remark;
        }
        Order order = saveOrder(type,price,businessId,user, description, paymentPlugin,appType);

        //3.调用支付统一下单接口，在支付服务器生成预支付订单
        Map<String, Object> map = paymentPlugin.pay(order.getOrderNumber(), name, price, request,appType);
        map.put("order", order);
        return Result.success(map);
    }

    /**
     * 支付处理
     *
     * @param order 收款单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(Order order,String appType) {
        if (order != null && order.getPayStatus() == Order.PayStatus.wait.value) {
            //1.修改订单变为已支付状态，收款人，收款单号，收款时间
            order.setModifyDate(new Date());
            order.setPayStatus(Order.PayStatus.success.value);
            order.setOrderStatus(Order.OrderStatus.completed.value);
            orderRepository.updateOrderById(order);

            //2.增加用户的充值金额（余额支付削减用户的余额）
            User user = userRepository.findByIdForUpdate(order.getUserId());
            if(null == user){
                throw new ServiceException(ErrorCode.C3005);
            }
            changeUserRechargeAndBalance(order,user);

            //3.开通用户的付费功能
            dredge(order,user,appType);

            //4.邀请模块
            String cofig = appConfigService.getValue("invitation","invitation_config",appType);
            String str[] = cofig.split(",");
            invitationService.feeService(user,order.getOrderPrice(),InvitationDetail.Type.One,new BigDecimal(str[0]),order.getSummary());
            invitationService.feeService(user,order.getOrderPrice(),InvitationDetail.Type.Two,new BigDecimal(str[1]),order.getSummary());

            //扩张业务处理

        }
    }

    /**
     * 修改用户充值金额和余额
     * @param order
     * @param user
     */
    private void changeUserRechargeAndBalance(Order order,User user){
        BigDecimal rechargeTotal = user.getRechargeTotal().add(order.getOrderPrice());
        BigDecimal balance = user.getBalance();
        if("balancePayPlugin".equalsIgnoreCase(order.getPluginName())){
            if(balance.compareTo(order.getOrderPrice())<=0){
                throw new ServiceException(ErrorCode.C5001);
            }
            balance = user.getBalance().subtract(order.getOrderPrice());
        }
        userRepository.updateRechargeTotalAndBalance(user.getId(),rechargeTotal,balance);
    }

    /**
     * 开通功能或者续费功能
     * @param order
     * @param user
     */
    private void dredge(Order order,User user,String appType){
        UserFunction userFunction = null;
        if(order.getType() == Order.Type.Vip.value){
            userFunction = userFunctionRepository.findByUserIdAndVip(order.getUserId(),UserFunction.Type.Vip);
        }else{
            userFunction = userFunctionRepository.findByUserIdAndTypeAndId(order.getBusinessId(),order.getUserId(),UserFunction.Type.valueOf(order.getType()));
        }
        if(null == userFunction){
            //首次开通
            userFunction = new UserFunction();
            userFunction.setUserId(user.getId());
            userFunction.setFunctionId(order.getBusinessId());
            String functionName = null;
            if(order.getType() == Order.Type.Single.value){
                //购买单个功能
                userFunction.setExpireTime(DateUtils.getYearAhead(-1,new Date()));
                Function function = functionService.findById(order.getBusinessId());
                functionName = function.getTitle();

                userFunction.setType(order.getType());
                userFunction.setFunctionName(functionName);
                userFunction.init();
                userFunctionRepository.save(userFunction);
            }else if(order.getType() == Order.Type.Vip.value){
                //购买vip功能，要开通
                Vip vip = vipService.findVipById(order.getBusinessId());
                Date expireTime = null;
                if(vip.getType() == Vip.Type.Month.value){
                    expireTime = DateUtils.getMonthAhead(-1,new Date());
                }else{
                    expireTime = DateUtils.getYearAhead(-1,new Date());
                }
                functionName = "会员";
                userFunction.setExpireTime(expireTime);
                userFunction.setType(order.getType());
                userFunction.setFunctionName(functionName);
                userFunction.init();
                userFunctionRepository.save(userFunction);

                //生成用户购买的单个记录
                List<UserFunction> userFunctionExistList = userFunctionRepository.findByUserIdAndType(user.getId(),UserFunction.Type.Normal);
                for(UserFunction userFunction1 : userFunctionExistList){
                    if(vip.getType() == Vip.Type.Month.value){
                        userFunction1.setExpireTime(DateUtils.getMonthAhead(-1,userFunction1.getExpireTime()));
                    }else {
                        userFunction1.setExpireTime(DateUtils.getYearAhead(-1,userFunction1.getExpireTime()));
                    }
                }
                userFunctionRepository.batchUpdate(userFunctionExistList);
                //添加用户购买功能
                addNewUserFunction(userFunctionExistList,order,expireTime,appType);

            }

        }else{
            //续费
            Date expireTime = null;
            if(order.getType() == Order.Type.Single.value){
                //单个功能续费
                expireTime = DateUtils.getYearAhead(-1,userFunction.getExpireTime());
                userFunctionRepository.updateExpireTimeById(userFunction.getId(),expireTime);
            }else if(order.getType() == Order.Type.Vip.value){
                //vip续费
                Vip vip = vipService.findVipById(order.getBusinessId());
                if(vip.getType() == Vip.Type.Month.value){
                    expireTime = DateUtils.getMonthAhead(-1,userFunction.getExpireTime());
                }else{
                    expireTime = DateUtils.getYearAhead(-1,userFunction.getExpireTime());
                }
                userFunctionRepository.updateExpireTimeById(userFunction.getId(),expireTime);
                //续费单个功能
                List<UserFunction> userFunctionList = userFunctionRepository.findByUserIdAndType(order.getUserId(), UserFunction.Type.Normal);
                List<UserFunction> newUserFunctionList = new ArrayList<>();
                for(UserFunction userFunction1 : userFunctionList){
                    userFunction1.setExpireTime(expireTime);
                    newUserFunctionList.add(userFunction1);
                }
                userFunctionRepository.batchUpdate(newUserFunctionList);
                //开通新增加的功能
                addNewUserFunction(newUserFunctionList,order,expireTime,appType);


            }

        }
    }

    private void addNewUserFunction(List<UserFunction> userFunctionExistList,Order order,Date expireTime,String appType){
        List<Long> functionIds = userFunctionExistList.stream().map(UserFunction::getFunctionId).collect(Collectors.toList());
        List<Integer> types = Arrays.asList(Function.Type.Extend.value, Function.Type.Gereral.value);

        List<Function> functionList = functionRepository.findByNotInIdsAndType(functionIds,types,appType);
        //List<Function> functionList =  functionService.findByType(Function.Type.Gereral);
        List<UserFunction> userFunctionList = new ArrayList<>();
        for(Function function : functionList){
            UserFunction userFunction1 = new UserFunction();
            userFunction1.setUserId(order.getUserId());
            userFunction1.setFunctionId(function.getId());
            userFunction1.setExpireTime(expireTime);
            userFunction1.setType(UserFunction.Type.Normal.value);
            userFunction1.setFunctionName(function.getTitle());
            userFunction1.init();
            userFunctionList.add(userFunction1);
        }
        userFunctionRepository.batchSave(userFunctionList);
    }


    /**
     * 查找前n个用户订单
     * @param limit
     * @return
     */
    @Override
    public List<Order> findOrderByLimit(int limit) {
        return orderRepository.findOrderByLimit(limit);
    }

    @Override
    public Result success(User user,String orderNumber,String source,String appType) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("user",user);
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if(!user.getId().equals(order.getUserId())){
            throw new ServiceException(ErrorCode.C5002);
        }
        if(order.getOrderStatus() != Order.OrderStatus.completed.value){
            throw new ServiceException(ErrorCode.C5002);
        }
        if(order.getPayStatus() != Order.PayStatus.success.value){
            throw new ServiceException(ErrorCode.C5002);
        }
        List<UserFunction> userFunctionList = userFunctionService.findByUserId(user.getId());
        map.put("userFunctionList",userFunctionList);

        //统计逻辑
        String date = DateUtils.format(new Date());
        String cofig = appConfigService.getValue("invitation","invitation_config",appType);
        String str[] = cofig.split(",");
        BigDecimal oneDeduct = invitationService.cacluateDeduct(user,InvitationDetail.Type.One,order.getOrderPrice(),new BigDecimal(str[0]));
        BigDecimal twoDeduct = invitationService.cacluateDeduct(user,InvitationDetail.Type.One,order.getOrderPrice(),new BigDecimal(str[1]));
        BigDecimal totalDeduct = oneDeduct.add(twoDeduct);

        recordService.updateRecord(date,order.getOrderPrice(), order.getPayType()==1?order.getOrderPrice():BigDecimal.ZERO,
                order.getPayType()==0?order.getOrderPrice():BigDecimal.ZERO,order.getPayType()==2?order.getOrderPrice():BigDecimal.ZERO,
                totalDeduct);

        channelRecordService.updateChannelRecord(date,appType,source,order.getOrderPrice(),totalDeduct);

        return Result.success(map);
    }

    /**
     * 生成订单
     */
    private Order saveOrder(Order.Type type, BigDecimal price,Long businessId, User user, String description, PaymentPlugin paymentPlugin,String appType) {
        Order order = new Order();
        order.setOrderPrice(price);
        order.setOrderStatus(Order.OrderStatus.confirmed.value);
        order.setPayStatus(Order.PayStatus.wait.value);
        order.setType(type.value);
        order.setOrderNumber(snService.generate(Sn.Type.order));
        order.setPayNumber("");
        order.setBusinessId(businessId);
        order.setSummary(description);
        order.setUserId(user.getId());
        order.setPluginName(paymentPlugin.getPluginName());
        order.setPayer("");
        order.setPaymentDate(Constant.date);
        order.setPaymentName(paymentPlugin.getPaymentName(appType));
        order.setPayType(Order.PayType.valueOf(paymentPlugin.getPluginName()).value);
        order.init();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        orderRepository.save(order,keyHolder);
        order.setId(keyHolder.getKey().longValue());
        return order;
    }
}
