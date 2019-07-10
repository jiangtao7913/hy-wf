package com.hy.wf.common.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-04-16 14:37
 * 编码器
 **/
public class HeartbeatEncode extends MessageToByteEncoder<CustomProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CustomProtocol msg, ByteBuf out) throws Exception {
        out.writeLong(msg.getId()) ;
        out.writeBytes(msg.getContent().getBytes()) ;
    }
}
