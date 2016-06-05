package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/5.
 */
@XmlRootElement(name="DevicesCollection")
public class DevicesConfig {
    static  class Device {
        @XmlAttribute
        String name;
        @XmlAttribute
        String address;
        @XmlAttribute
        String type;
        @XmlAttribute
        String key;
        @XmlAttribute
        String protocol;
        @XmlAttribute
        String descriptor;
    }
    @XmlElement(name="Device")
    List<Device> devices;


}
