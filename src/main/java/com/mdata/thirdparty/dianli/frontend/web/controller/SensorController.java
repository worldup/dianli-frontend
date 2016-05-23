package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by administrator on 16/5/15.
 */
@Controller
@RequestMapping(value = "/sensor",method = {RequestMethod.GET})
public class SensorController {
    @Autowired
    private SensorService sensorService;
    @RequestMapping(value="/count")
    public void count(){
        sensorService.testConnect();
    }
    @RequestMapping(value="/test2")
    public void test2(){
        sensorService.test2();
    }
    @RequestMapping("/chart/temperature")
    public String temperature(){
        return "/chart/temperature";
    }
    @RequestMapping("/chart/threephase")
    public String threephase(){
        return "/chart/threephase";
    }
    @RequestMapping("/chart/humidity")
    public String humidity(){
        return "/chart/humidity";
    }
}
