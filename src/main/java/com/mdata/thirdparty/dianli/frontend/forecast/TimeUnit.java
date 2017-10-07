package com.mdata.thirdparty.dianli.frontend.forecast;

import java.sql.Time;

public class TimeUnit {
    private long delay;
    private Unit unit;
      enum Unit{
        MINUTE,
        HOUR,
        DAY,
        WEEK,
        MONTH,
        YEAR
    }
    public TimeUnit(long delay,Unit unit){
        this.delay=delay;
        this.unit=unit;

    }
    @Override
    public String toString(){
        return " "+delay +" "+unit.toString();
    }

    public static void main(String[] args) {
        TimeUnit _3day=  new TimeUnit(3,Unit.DAY);
        System.out.println(_3day);
    }
}
