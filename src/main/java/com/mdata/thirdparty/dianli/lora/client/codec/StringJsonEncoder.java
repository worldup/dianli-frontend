package com.mdata.thirdparty.dianli.lora.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by administrator on 17/7/23.
 */
public class StringJsonEncoder  extends MessageToMessageEncoder<CharSequence> {
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {

    }
}
