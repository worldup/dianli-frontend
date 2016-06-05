package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/5.
 */
@XmlRootElement(name="SensorsForm")
public class SensorsForm {
    @XmlAttribute
    String version;
    @XmlElement(name="Sensor")
    List<Sensor> sensors;
    static class Sensor{
        @XmlAttribute
        String id;
        @XmlAttribute
        String name;
        @XmlAttribute
        String modelNo;
        @XmlElement(name="Filter")
        List<Filter> filters;
        @XmlElement(name="Entry")
        List<Entry> entries;
        static class Filter{
            @XmlAttribute
            String level;
            @XmlAttribute
            String type;
        }
        static class Entry{
            @XmlAttribute
            String name;
            @XmlAttribute
            String id;
            @XmlAttribute
            String type;
            @XmlAttribute
            String unit;
            @XmlAttribute
            String upperValue;
            @XmlAttribute
            String lowerValue;
            @XmlAttribute
            String changeValue;
            @XmlAttribute
            String abnormalValue;
            @XmlAttribute
            String updateSpan;
            @XmlAttribute
            String offlineSpan;
            @XmlAttribute
            String mapNo;
        }
    }
}
