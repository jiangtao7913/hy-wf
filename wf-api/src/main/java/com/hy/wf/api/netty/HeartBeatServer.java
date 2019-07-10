package com.hy.wf.api.netty;

import com.hy.wf.common.test.HeartbeatDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-04-16 14:48
 **/
@Component
@Slf4j
public class HeartBeatServer {
    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    @Autowired
    private HeartBeatSimpleHandle heartBeatSimpleHandle;

    private int nettyPort=20001;


    /**
     * 启动 Netty
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new HeartbeatInitializer());

        ChannelFuture future = bootstrap.bind(nettyPort).sync();
        if (future.isSuccess()) {
            log.info("启动 Netty 成功");
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        log.info("关闭 Netty 成功");
    }



    public class HeartbeatInitializer extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ch.pipeline()
                    //五秒没有收到消息 将IdleStateHandler 添加到 ChannelPipeline 中
                    .addLast(new IdleStateHandler(5, 0, 0))
                    .addLast(new HeartbeatDecoder())
                    .addLast(heartBeatSimpleHandle);
        }
    }
}
