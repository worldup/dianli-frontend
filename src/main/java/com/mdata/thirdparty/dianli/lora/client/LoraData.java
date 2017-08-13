package com.mdata.thirdparty.dianli.lora.client;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * Created by administrator on 17/8/12.
 */
public class LoraData implements Serializable {
   // AAAA352011B5F0B6050000000DC03300080C151F0E71A58B
//2 字节起始符 AAAA
    private String prefix;
//2字节基站地址 3520
    private String station;
//1字节数据长度  11= 16+1
    private int dataLenth;
//1字节命令码 B5
    private String cmdCode;
 //若干SensorData F0B6050000000DC03300080C151F0E71
    private List<LoraSensorData> sensorData;

//2字节Crc16校验码 A58B
    private String crc16;
    public LoraData(String loradata){
        this.prefix=loradata.substring(0,4);
        this.station=loradata.substring(4,8);
        this.dataLenth=Integer.parseInt(loradata.substring(8,10),16)*2;
        this.cmdCode=loradata.substring(10,12);
        int sensorDataEndIdx=12+(dataLenth-2);
        String sensorDataStr=loradata.substring(12,sensorDataEndIdx);
        this.sensorData=parseSensorDataList(sensorDataStr);
        this.crc16=loradata.substring(sensorDataEndIdx,sensorDataEndIdx+4);
    }
    private List<LoraSensorData> parseSensorDataList(String sensorDataStr){
        List<LoraSensorData> sensorDataList= Lists.newArrayList();
        if(sensorDataStr.length()%32!=0)
        {
            System.out.println("sensorData error!!!");
        }
        else{
            for(int i=0;i<sensorDataStr.length()/32;i++){
                LoraSensorData sensorData=new LoraSensorData(sensorDataStr.substring(i*32,32+i*32));
                sensorDataList.add(sensorData);
            }
        }
        return  sensorDataList;

    }

    @Override
    public String toString() {
        return "LoraData{" +
                "prefix='" + prefix + '\'' +
                ", station='" + station + '\'' +
                ", dataLenth=" + dataLenth +
                ", cmdCode='" + cmdCode + '\'' +
                ", sensorData=" + sensorData +
                ", crc16='" + crc16 + '\'' +
                '}';
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getDataLenth() {
        return dataLenth;
    }

    public void setDataLenth(int dataLenth) {
        this.dataLenth = dataLenth;
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }

    public List<LoraSensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<LoraSensorData> sensorData) {
        this.sensorData = sensorData;
    }

    public String getCrc16() {
        return crc16;
    }

    public void setCrc16(String crc16) {
        this.crc16 = crc16;
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("11",16));
    }
}
