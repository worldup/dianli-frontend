package com.mdata.thirdparty.dianli.lora.client;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mdata.thirdparty.dianli.frontend.web.controller.api.SensorData;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.collections4.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 17/7/23.
 */

public class LoraHandler extends ChannelInboundHandlerAdapter {
    private SensorService sensorService;
    public LoraHandler(SensorService sensorService){
        this.sensorService=       sensorService;

    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        String join="71\n{\"CMD\":\"JOIN\",\"AppEUI\":\"2C26C501a1000001\",\"AppNonce\":1,\"Challenge\":\"1\"}" ;
        ctx.writeAndFlush(join);
    }
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("receive from lora -->"+msg);
        LoraData loraData= new LoraData(String.valueOf(msg));
        if(loraData!=null){
           List<LoraSensorData> loraSensorDataList= loraData.getSensorData();
           if(CollectionUtils.isNotEmpty(loraSensorDataList)){
               final String station= loraData.getStation();
               List<SensorData> sensorDataList= Lists.transform(loraSensorDataList, new Function<LoraSensorData, SensorData>() {
                   @Override
                   public SensorData apply(LoraSensorData input) {
                       String address=input.getAddress();
                       SensorValue sensorValue=input.getSensorValue();
                       String type=input.getType();
                       Date date=input.getDatetime();
                       SensorData sensorData=new SensorData();
                       sensorData.setIdx(0);
                       try{
                           sensorData.setSv(Double.valueOf(sensorValue.getValue()));
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                       sensorData.setSid(station+"_"+address+"_"+type);
                       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                       sensorData.setDay(sdf.format(date));
                       sensorData.setTg(date.getTime());
                       return sensorData;
                   }
               });
               sensorService.insertSensorValues(sensorDataList);

           }
        }
        System.out.println(loraData);
    }


    public static void main(String[] args) {
        String all="D111000000000033000801112C18A6EF";
        System.out.println(
                all.substring(0,4)+"--"+
         all.substring(4,6)+"--"+
         all.substring(6,12)+"--"+
         all.substring(12,16)+"--"+
         all.substring(16,18)+"--"+
         all.substring(18,20)+"--"+
         all.substring(20,30)+"--"+
         all.substring(30,32));
    }
}
