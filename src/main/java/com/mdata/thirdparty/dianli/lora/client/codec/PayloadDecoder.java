package com.mdata.thirdparty.dianli.lora.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by administrator on 17/7/23.
 */
public class PayloadDecoder extends MessageToMessageDecoder<String> {
    public static byte[] decodeBase64(String input) throws Exception {
        byte[] bt = null;
        try {
             sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            bt = decoder.decodeBuffer(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bt;
    }
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        if(msg!=null&&msg.length()>0){
            int msgLen=msg.length();
            for(int i=0;i<msgLen%4;i++){
               msg+="=";
            }

             byte [] b= decodeBase64(msg);
            StringBuilder sb=new StringBuilder();
            for (int i = 0; i < b.length; i++) {
                String hex = Integer.toHexString(b[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase()) ;
            }
            out.add(sb.toString());
        }
    }
}
