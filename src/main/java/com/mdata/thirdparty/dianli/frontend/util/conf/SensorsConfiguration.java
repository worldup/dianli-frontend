package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/5.
 */
@XmlRootElement(name="SensorsConfiguration")
public class SensorsConfiguration {
    @XmlElement(name="Group")
    public List<Group> groups;
    public static class Group {
        @XmlElement(name="Group")
        public List<Group> groups;
        @XmlAttribute
        public String name;
        @XmlAttribute
        public String key;
        @XmlAttribute
        public String owner;
        @XmlAttribute
        public String ownerGroup;
        @XmlAttribute
        public String permission;
        @XmlElement(name = "Sensor")
        public List<Sensor> sensors;
        public static class Sensor{

            @XmlAttribute
            public String name;
            @XmlAttribute
            public String key;
            @XmlAttribute
            public String skey;
            @XmlAttribute
            public String owner;
            @XmlAttribute
            public String ownerGroup;
            @XmlAttribute
            public String permission;

        }
    }
    }
