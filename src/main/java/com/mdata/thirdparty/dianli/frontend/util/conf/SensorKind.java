package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/4.
 */
@XmlRootElement(name="SensorsKind")
public class SensorKind {

    @XmlAttribute(name="version")
     String version;
    @XmlElement(name="Kind")
      List<Kind> kind;
    static class Kind {
        @XmlAttribute
        String id;
        @XmlAttribute
        String name;
        @XmlElement(name = "Sensor")
        List<Sensor> sensor;
        static class Sensor{
            @XmlAttribute
            String id;
        }
    }

}
