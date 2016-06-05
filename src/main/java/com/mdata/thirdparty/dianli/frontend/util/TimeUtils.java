package com.mdata.thirdparty.dianli.frontend.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by administrator on 16/6/4.
 */
public class TimeUtils {

    public static int getDayOfYear(Date date){
        Calendar calendar=GregorianCalendar.getInstance();
        calendar.setTime(date);
        return  calendar.get(Calendar.DAY_OF_YEAR);

    }

    //2000-01-01后的整数转成时间
    public static Date int2Date(int  value){
        Date date=new Date(946656000000l+value*1000l);
        return  date;
    }

    public static String format(Date date){
       return  DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss");
    }

    public static void main(String[] args) throws Exception {
        Date date=DateUtils.parseDate("2000-01-01 00:00:00","yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
        System.out.println(format(int2Date(504973350)));
        System.out.println(new Date(946656000000l+504973350*1000l));
    }
}
