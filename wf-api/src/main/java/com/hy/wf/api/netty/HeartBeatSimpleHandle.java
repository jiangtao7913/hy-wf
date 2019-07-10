package com.hy.wf.api.netty;

import com.alibaba.fastjson.JSON;
import com.hy.wf.api.service.v1.UserService;
import com.hy.wf.common.test.CustomProtocol;
import com.hy.wf.common.test.NettyRedis;
import com.hy.wf.common.test.NettySocketHolder;
import com.hy.wf.entity.User;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-04-16 14:46
 **/
@Service
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatSimpleHandle extends SimpleChannelInboundHandler<CustomProtocol> {

    @Autowired
    private NettyRedis nettyRedis;
    @Autowired
    private UserService userService;


    /**
     * 取消绑定
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接");
        NettySocketHolder.remove((NioSocketChannel) ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt ;

            if (idleStateEvent.state() == IdleState.READER_IDLE){
                log.info("已经5秒没有收到信息！");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 收到客户端消息
     * @param ctx
     * @param customProtocol
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CustomProtocol customProtocol) throws Exception {
        try {
            log.info("收到customProtocol={}", customProtocol);
            Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(customProtocol.getContent());
            String userId = (String)dataMap.get("userId");
            String hardwareKey = (String) dataMap.get("hardwareKey");
            String event = (String) dataMap.get("event");
            String channelId = (String) dataMap.get("channelId");

            //保存客户端与 Channel 之间的关系
            NettySocketHolder.put(hardwareKey,(NioSocketChannel)ctx.channel());
            if (StringUtils.equals("heartbeat", event)) { // 心跳
                //心跳处理
                heartbeatProcess(ctx,Long.valueOf(userId),hardwareKey);
            }
        }catch (Exception e){
            log.error("消息错误");
        }

    }

    /**
     * 心跳处理
     */
    public void heartbeatProcess(ChannelHandlerContext ctx,Long userId,String hardwareKey){
        Map<String, Object> map = null;
        if(StringUtils.isNotEmpty(String.valueOf(userId)) && StringUtils.isNotEmpty(hardwareKey)){
            map = checkAccount(userId,hardwareKey);

            if (map == null || map.size() == 0) {
                // 检查并更新用户状态和是否前端运行
                checkAndUpdateUserStatus(userId);
            }
        }

        if (map != null && map.size() > 0) {
            if(NettySocketHolder.getMAP().get(hardwareKey) != null && map.get("hardwareKey").toString().equals(hardwareKey)){
                ctx.writeAndFlush(Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new CustomProtocol(userId,JSON.toJSONString(map)).toString(),
                        CharsetUtil.UTF_8))).addListener(ChannelFutureListener.CLOSE_ON_FAILURE) ;
            }
        }
    }

    /**
     * 检测前端是否运行和用户在线状态
     */
    public void checkAndUpdateUserStatus(Long userId){
        String status = nettyRedis.getUserStatus(userId);
        if (status == null || User.Online.n.value == User.Online.valueOf(status).value) {
            User user = userService.findById(userId);
            if(user != null){
                if (User.Online.n.value == user.getOnline()) {
                    user.setOnline(User.Online.y.value);
                }
                //缓存用户状态
                nettyRedis.setUserStatus(userId);
                //修改用户信息
                userService.updateInfo(userId,"online",String.valueOf(user.getOnline()));
            }
        }

    }

    /**
     * 检测账户
     */
    public Map<String, Object> checkAccount(Long userId,String hardwareKey){
        Map<String, Object> map = Collections.emptyMap();
        String kicked = nettyRedis.getLoginFlag(userId,hardwareKey);
        if(kicked.equals("true")){
            map = new HashMap<>();
            map.put("kicked", true);
            map.put("kickedMemo", "账户在其它设备上登录");
            map.put("hardwareKey", hardwareKey);
            nettyRedis.deleteLoginHardKey(userId);
            nettyRedis.deleteLoginFlag(userId,hardwareKey);
        } else{
            String uidHardwareKey = nettyRedis.getLoginHardKey(userId);
            if (uidHardwareKey.equals("null")) {
                log.info("账户第一次使用的设备或");
                nettyRedis.setLoginHardKey(userId,hardwareKey);
            }else if(!StringUtils.equals(uidHardwareKey, hardwareKey)){
                log.info("{}此设备{}上的用户需踢出", userId, uidHardwareKey);
                nettyRedis.setLoginFlag(userId,uidHardwareKey); // 此设备上的用户需踢出
            }
        }
        return map;
    }




}
