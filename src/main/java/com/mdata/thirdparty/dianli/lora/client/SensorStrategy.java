package com.mdata.thirdparty.dianli.lora.client;

/**
 * Created by administrator on 17/8/12.
 */
public class SensorStrategy {
    public static SensorValue calc(String type,String d7,String d8 ){
        int rawValue=0;
        int D7=Integer.parseInt(d7,16);
        int D8=Integer.parseInt(d8,16);
        String result="";
        String unit="";
        if("05".equals(type)){
            unit="℃";
            rawValue =  D7 * 0x100 + D8;
            if (rawValue > 32768 )
            {
                rawValue = rawValue - 65536;
            }
            result =String.valueOf(rawValue / 128) ;
        }
        else if("40".equals(type)){
            unit="%RH";
            rawValue = D8;
            result = String.valueOf(rawValue / 2);
        }
        else if("4F".equals(type)){
            unit="με";
            rawValue = D7 * 0x100 + D8;
            if (rawValue > 32768 ){
                rawValue = rawValue - 65536;
            }
            result = String.valueOf(rawValue);
        }
        else if("4E".equals(type)){
            unit="με";
            rawValue = D7 * 0x100 + D8;
            if (rawValue > 32768 )
                    rawValue = rawValue - 65536;
            result = String.valueOf(rawValue);
        }
        else if("43".equals(type)){
            rawValue = D8;
            if( rawValue == 0x00 )
                    result = "干燥";
            if (rawValue == 0x01 )
                    result = "浸水";
        }
        else if("6B".equals(type)){
            unit="dB";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("60".equals(type)){
            unit="hPa";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue * 2);
        }
        else if("45".equals(type)){
            rawValue = D8;
            if (rawValue == 0x10 )
                    result = "打开";
            if (rawValue == 0x0F )
                    result = "关闭";
        }
        else if("10".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("11".equals(type)){
            unit="次";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue );
        }
        else if("1A".equals(type)){
            unit="uA";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue );
        }
        else if("13".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("16".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("20".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("21".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("22".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("23".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("24".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("25".equals(type)){
            unit="kV";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
         else if("54".equals(type)){
            unit="Var";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 100);
        }
        else if("55".equals(type)){
            unit="kW";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 100);
        }
        else if("57".equals(type)){
            unit="Kwh";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10000);
        }
        else if("58".equals(type)){
            unit="°";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10000);
        }
        else if("59".equals(type)){
            unit="Hz";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 100);
        }
        else if("65".equals(type)){
            unit="°";
            rawValue = D7 * 0x100 + D8;
            if (rawValue > 32768 )
                    rawValue = rawValue - 65536;
            if (Math.abs(rawValue) <= 1000 )
            {
                rawValue = rawValue / 1000;
                result = String.valueOf(Math.asin(rawValue) / Math.PI * 180) ;
            }
        }
        else if("66".equals(type)){
            unit="°";
            rawValue = D7 * 0x100 + D8;
            if (rawValue > 32768 )
                    rawValue = rawValue - 65536;
            if (Math.abs(rawValue) <= 1000 )
            {
                rawValue = rawValue / 1000;
                result = String.valueOf(Math.asin(rawValue) / Math.PI * 180);
            }
        }
        else if("04".equals(type)){
            unit="℃";
            rawValue = D7 * 0x100 + D8;
            if (rawValue > 32768 )
                    rawValue = rawValue - 65536;
            result = String.valueOf(rawValue / 128);
        }
        else if("45".equals(type)){
            rawValue = D8;
            if (rawValue == 0x10 )
                    result = "打开";
            else if (rawValue ==0x0F )
                    result = "关闭";
        }
        else if("47".equals(type)){
            rawValue = D8;
            if (rawValue == 0x10 )
                result = "触发";
            else if (rawValue ==0x0F )
                result = "未触发";
        }
        else if("50".equals(type)){
            rawValue = D8;
            if (rawValue == 0x10 )
                result = "震动";
            else if (rawValue ==0x0F )
                result = "静止";
        }
        else if("45".equals(type)){
            rawValue = D8;
            if (rawValue == 0x10 )
                result = "打开";
            else if (rawValue ==0x0F )
                result = "关闭";
        }
        else if("A0".equals(type)){
            unit="kW";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("A1".equals(type)){
            unit="kVar";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("A2".equals(type)){
            unit="kVa";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("A3".equals(type)){
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10000);
        }
        else if("A4".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /1);
        }
        else if("A5".equals(type)){
            unit="V";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /100);
        }
        else if("B0".equals(type)){
            unit="kW";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("B1".equals(type)){
            unit="kVar";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("B2".equals(type)){
            unit="kVa";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("B3".equals(type)){
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10000);
        }
        else if("B4".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /1);
        }
        else if("B5".equals(type)){
            unit="V";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /100);
        }
        else if("C1".equals(type)){
            unit="kVar";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("C2".equals(type)){
            unit="kVa";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10);
        }
        else if("C3".equals(type)){
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue / 10000);
        }
        else if("C4".equals(type)){
            unit="A";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /1);
        }
        else if("C5".equals(type)){
            unit="V";
            rawValue = D7 * 0x100 + D8;
            result = String.valueOf(rawValue /100);
        }
        return new SensorValue(unit,result);
    }
}
