package com.mdata.thirdparty.dianli.frontend.util.conf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by administrator on 16/6/5.
 */
@XmlRootElement(name="SensorsCollection")
public class SensorsTable {
    @XmlElement(name="Sensor")
    public List<Sensor> sensorList;
    public static class Sensor{
        @XmlAttribute
        public String id;
        @XmlAttribute
        public String name;
        @XmlAttribute
        public String type;
        @XmlAttribute
        public String sid;
        @XmlAttribute
        public String key;
        @XmlElement(name="Entry")
        public  Entry entry;
        public static class Entry{
            @XmlAttribute
            public String name;
            @XmlAttribute
            public String lowerValue;
            @XmlAttribute
            public String upperValue;
            @XmlAttribute
            public String changeValue;
            @XmlAttribute
            public String abnormalValue;
            @XmlAttribute
            public String updateSpan;
            @XmlAttribute
            public String offlineSpan;
            @XmlAttribute
            public String adjust;
            @XmlAttribute
            public String adjustMultiplier;
            @XmlAttribute
            public String adjustOffsetAdder;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Entry entry = (Entry) o;

                if (name != null ? !name.equals(entry.name) : entry.name != null) return false;
                if (lowerValue != null ? !lowerValue.equals(entry.lowerValue) : entry.lowerValue != null) return false;
                if (upperValue != null ? !upperValue.equals(entry.upperValue) : entry.upperValue != null) return false;
                if (changeValue != null ? !changeValue.equals(entry.changeValue) : entry.changeValue != null)
                    return false;
                if (abnormalValue != null ? !abnormalValue.equals(entry.abnormalValue) : entry.abnormalValue != null)
                    return false;
                if (updateSpan != null ? !updateSpan.equals(entry.updateSpan) : entry.updateSpan != null) return false;
                if (offlineSpan != null ? !offlineSpan.equals(entry.offlineSpan) : entry.offlineSpan != null)
                    return false;
                if (adjust != null ? !adjust.equals(entry.adjust) : entry.adjust != null) return false;
                if (adjustMultiplier != null ? !adjustMultiplier.equals(entry.adjustMultiplier) : entry.adjustMultiplier != null)
                    return false;
                return adjustOffsetAdder != null ? adjustOffsetAdder.equals(entry.adjustOffsetAdder) : entry.adjustOffsetAdder == null;

            }

            @Override
            public int hashCode() {
                int result = name != null ? name.hashCode() : 0;
                result = 31 * result + (lowerValue != null ? lowerValue.hashCode() : 0);
                result = 31 * result + (upperValue != null ? upperValue.hashCode() : 0);
                result = 31 * result + (changeValue != null ? changeValue.hashCode() : 0);
                result = 31 * result + (abnormalValue != null ? abnormalValue.hashCode() : 0);
                result = 31 * result + (updateSpan != null ? updateSpan.hashCode() : 0);
                result = 31 * result + (offlineSpan != null ? offlineSpan.hashCode() : 0);
                result = 31 * result + (adjust != null ? adjust.hashCode() : 0);
                result = 31 * result + (adjustMultiplier != null ? adjustMultiplier.hashCode() : 0);
                result = 31 * result + (adjustOffsetAdder != null ? adjustOffsetAdder.hashCode() : 0);
                return result;
            }
        }
    }

}
