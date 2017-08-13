package com.mdata.thirdparty.dianli.lora.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 17/7/23.
 */
public class LoraDecoder extends MessageToMessageDecoder<Map<String,String>> {
    final String JOIN_ACCEPT="JOIN ACCEPT";
    final String UPLOAD="UPLOAD";
    protected void decode(ChannelHandlerContext ctx, Map<String,String> msg, List<Object> out) throws Exception {
        String msgFlag=msg.get("MSG");
        if(JOIN_ACCEPT.equalsIgnoreCase(msgFlag)){
            out.add("");
        }
        else if(UPLOAD.equalsIgnoreCase(msgFlag)){
           String payload= msg.get("payload");
           out.add(payload);
        }
        else{
            out.add("");
        }
    }
}
