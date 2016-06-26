package com.mdata.thirdparty.dianli.frontend.web.controller.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 16/6/25.
 */
@RestController
@RequestMapping(value = "/api")
public class ApiController {
    static final Logger logger=LoggerFactory.getLogger(ApiController.class);
    @Autowired
    private SensorService sensorService;
    @RequestMapping("/sensor")
    public Result uploadData(String data){
       ObjectMapper mapper=ObjectMapperUtil.genObjectMapper();
        Result result=new Result();
        List<SensorData> sensorDataList =new ArrayList();
        try{
           JavaType javaType= mapper.getTypeFactory().constructParametrizedType(ArrayList.class,List.class, SensorData.class);
            sensorDataList=mapper.readValue(data, javaType);
            sensorService.insertSensorValues(sensorDataList);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            result.setStatus(500);
            result.setErrMsg("数据处理异常");
            return result;
        }

        result.setStatus(200);
        result.setErrMsg("");
        return result;
    }
}
