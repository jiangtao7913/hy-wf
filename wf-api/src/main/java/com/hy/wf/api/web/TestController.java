package com.hy.wf.api.web;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.hy.wf.api.dao.base.log.CustomLog;
import com.hy.wf.api.dao.repository.Test.TestRepository;
import com.hy.wf.api.dao.repository.Test.User;
import com.hy.wf.api.dao.repository.v1.AppLogRepository;
import com.hy.wf.api.dao.repository.v1.FunctionRepository;
import com.hy.wf.api.dao.repository.v1.UserRepository;
import com.hy.wf.api.service.TestService;
import com.hy.wf.common.Result;
import com.hy.wf.common.annotation.IsLogin;
import com.hy.wf.api.config.RedisConfig;
import com.hy.wf.common.test.NettyRedis;
import com.hy.wf.entity.AppLog;
import com.hy.wf.entity.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TestController extends BaseController{

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private AppLogRepository appLogRepository;


    @Autowired
    private NettyRedis nettyRedis;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void test(){
        System.out.println("测试");
        AppLog appLog = appLogRepository.findById(175L);
        String paraData=null;
        try {
            paraData  = URLDecoder.decode(appLog.getParamData(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(paraData);
    }

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    @IsLogin
    @CustomLog(action = "测试")
    //@Transactional(rollbackFor = Exception.class)
    public void test1(){
        List<Function> list = new ArrayList<>();
        Function function = new Function();
        function.setOrders(5);
        function.setTitle("1111111333");
        function.setType(1);
        function.setPosition("111");
        function.setUrl("111");
        function.setPrice(new BigDecimal("0"));
        function.setMeno("111");
        function.init();
        function.setIcon("1");
        function.setId(17L);
        list.add(function);
        Function function1 = new Function();
        function1.setOrders(5);
        function1.setTitle("2222222");
        function1.setType(1);
        function1.setPosition("222");
        function1.setUrl("222");
        function1.setPrice(new BigDecimal("0"));
        function1.setMeno("222");
        function1.init();
        function.setIcon("2");
        function1.setId(18L);
        list.add(function1);
        functionRepository.batchUpdate(list);
        System.out.println("测试111111");
    }

    @PostMapping(value = "test2")
    @IsLogin
    @CustomLog(action = "测试")
    public UserTest test2(HttpServletRequest request){
        String uid = (String) request.getAttribute("UID");
        String uid1 = getUid();
        System.out.println("测试2222");
        UserTest user = new UserTest();
        int a=1/0;
//        user.setId(1L);
//        user.setName("jt");
        return user;
    }

    @PostMapping(value = "test3")
    @IsLogin
    public Result test3(HttpServletRequest request){
        System.out.println("没有走缓存");
        User user = testRepository.findById(1L);
        return Result.success(user);
    }

    @PostMapping(value = "test4")
    @IsLogin
    public User test4(HttpServletRequest request){
        User  user = testRepository.findById(1L);
        return user;
    }

    @PostMapping(value = "test5")
    @IsLogin
    public Result test5(HttpServletRequest request){
        User user = testService.test(2L);
        return Result.success(user);
    }

    @PostMapping(value = "test6")
    @IsLogin
    public Result test6(HttpServletRequest request){
        testService.test1("1");
       return null;
    }

    /**
     * @RequestHeader HttpHeaders headers 获取全部头部的值
     * @RequestHeader("test") String test 获取头部为test的值
     * @param id
     * @param headers
     * @param test
     * @return
     */
    @PostMapping(value = "test7/{id}")
    @IsLogin
    public Result test7(@Valid @PathVariable("id") BigDecimal id, @RequestHeader HttpHeaders headers, @RequestHeader("test") String test, BindingResult bindingResult){
        BigDecimal id1 = id;
        System.out.println(id1);
//        //获取头部第一个key为test的值
//        String test = headers.getFirst("test");
          System.out.println(test);
        return null;
    }


    @PostMapping(value = "test8")
    @IsLogin
    public Result test7(@Valid UserTest user){
        //        BigDecimal id1 = id;
//        System.out.println(id1);
////        //获取头部第一个key为test的值
////        String test = headers.getFirst("test");
//        System.out.println(test);
        System.out.println("1111111111aaaa");
        return null;
    }

    @PostMapping(value = "netty")
    @IsLogin
    public Result test8(@Valid UserTest user){
        nettyRedis.getLoginFlag(1L,"aaa");
        System.out.println("1111111111aaaa");
        return null;
    }

}
