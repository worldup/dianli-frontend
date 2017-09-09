package com.mdata.thirdparty.dianli.lora.client;

import com.mdata.thirdparty.dianli.lora.client.util.CRC8;
import com.mdata.thirdparty.dianli.lora.client.util.HexByteHelper;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by administrator on 17/8/2.
 */
public class LoraSensorData implements Serializable {
    private String address;
    private String type;
    private String reserve;
    private  String d7;
    private String d8;
    private String voltage;
    private String d10;
    private Date datetime;
    private String crc;
    private SensorValue sensorValue;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getD7() {
        return d7;
    }

    public void setD7(String d7) {
        this.d7 = d7;
    }

    public String getD8() {
        return d8;
    }

    public void setD8(String d8) {
        this.d8 = d8;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getD10() {
        return d10;
    }

    public void setD10(String d10) {
        this.d10 = d10;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public SensorValue getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(SensorValue sensorValue) {
        this.sensorValue = sensorValue;
    }

    public LoraSensorData(String v ){

        this.address=v.substring(0,4);
        this.type=v.substring(4,6);
         
               this.reserve= v.substring(6,12);
              this.d7=  v.substring(12,14);
               this.d8=v.substring(14,16);
               this.voltage=v.substring(16,18);
                this.d10=v.substring(18,20);
                this.datetime=parseDate(v.substring(20,30));
                this.crc=v.substring(30,32);
                this.sensorValue=SensorStrategy.calc(type,d7,d8);

    }
    private  Date parseDate(String datetime){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar=Calendar.getInstance();
        int year= Integer.parseInt(datetime.substring(0,1),16);
        int month=Integer.parseInt(datetime.substring(1,2),16)-1;
        int day=Integer.parseInt(datetime.substring(2,4),16);
        int  hour=Integer.parseInt(datetime.substring(4,6),16);
       int minute= Integer.parseInt(datetime.substring(6,8),16);
        int second= Integer.parseInt(datetime.substring(8,10),16);

          calendar.set(Calendar.MONTH,month);
          calendar.set(Calendar.DATE,day);
          calendar.set(Calendar.HOUR_OF_DAY,hour);
          calendar.set(Calendar.MINUTE,minute);
          calendar.set(Calendar.SECOND,second);
        return calendar.getTime();
    }
    public boolean checkCRC(String v){
        byte[] crc= HexByteHelper.hexStringToBytes(v);
        String hex=HexByteHelper.bytesToHexString(crc);
            byte crc15=CRC8.crc8(crc,16);
            System.out.println(crc15+"-->"+crc[15]);


        return false;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", reserve='" + reserve + '\'' +
                ", d7='" + d7 + '\'' +
                ", d8='" + d8 + '\'' +
                ", voltage='" + voltage + '\'' +
                ", d10='" + d10 + '\'' +
                ", datetime=" + datetime +
                ", crc='" + crc + '\'' +
                ", sensorValue=" + sensorValue +
                '}';
    }

    public static void main(String[] args) {

        LoraSensorData sensorData=new LoraSensorData("20 DC 05 73 82 65 0D 40 56 81 C5 11 0D 14 1D 99".replaceAll(" ",""));
        System.out.println(sensorData);
    }
}
