package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/5.
 */
@XmlRootElement(name = "SensorsWatcher")
public class SensorsGuard {
    @XmlElement(name = "Guard")
    public List<Guard> guards;

    public static class Guard {
        @XmlAttribute
        public String key;
        @XmlAttribute
       public  String nodeKey;
        @XmlAttribute
       public  String type;
        @XmlElement(name = "Setting")
       public Setting setting;

        public static class Setting {
            @XmlElement(name = "Parameter")
           public Parameter parameter;

           public static class Parameter {
                @XmlAttribute
                public String name;
                @XmlAttribute
                public String type;
                @XmlAttribute
                public String value;
            }
        }
    }
}
