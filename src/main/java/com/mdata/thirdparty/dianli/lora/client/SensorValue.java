package com.mdata.thirdparty.dianli.lora.client;

import java.io.Serializable;

/**
 * Created by administrator on 17/8/12.
 */
public class SensorValue implements Serializable {
    private String unit;
    private String value;

    @Override
    public String toString() {
        return "SensorValue{" +
                "unit='" + unit + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public SensorValue(String unit, String value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
