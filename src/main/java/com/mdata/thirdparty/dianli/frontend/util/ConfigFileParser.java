package com.mdata.thirdparty.dianli.frontend.util;

import com.mdata.thirdparty.dianli.frontend.util.conf.SensorKind;
import com.mdata.thirdparty.dianli.frontend.util.conf.SensorsTable;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by administrator on 16/6/4.
 */
//解析电网配置文件
public class ConfigFileParser  {

    public static<T>  T parse(String fileName,Class<T> tClass){
        T result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
            Unmarshaller um = jaxbContext.createUnmarshaller();
            result = (T)um.unmarshal(new FileInputStream(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static void main(String[] args) {
      // parse("data/sensorskind.xml",SensorKind.class);
     //   parse("data/devicesconfig.xml",DevicesConfig.class);
     //  parse("data/sensorstable.xml",SensorsTable.class);
      //  parse("data/sensorsguard.xml",SensorsGuard.class);
      //  parse("data/sensorsform.xml",SensorsForm.class);
        //parse("data/sensorsconfig.xml",SensorsConfiguration.class);

    }

}
