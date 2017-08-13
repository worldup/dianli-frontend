package com.mdata.thirdparty.dianli.lora.client.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 17/7/23.
 */
public class StringJsonDecoder extends MessageToMessageDecoder<String>{
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        List<String> msgList=Splitter.on("\n").splitToList(msg);

        if(msgList.size()>1){
            for(String temp:msgList){
                if(temp.indexOf("{")>-1&&temp.indexOf("}")>-1){
                    msg=temp;
                    break;
                }
            }
        }
        Map result= mapper.readValue(msg, Map.class);
        out.add(result);
    }

}
