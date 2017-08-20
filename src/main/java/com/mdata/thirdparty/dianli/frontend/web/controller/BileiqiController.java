package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.PageResult;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.IBileiqiService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 17/8/20.
 */
@RestController
@RequestMapping(value = "/bileiqi",method = {RequestMethod.GET})
public class BileiqiController {
    @Autowired
    private IBileiqiService iBileiqiService;
    @Autowired
    private ModelAndViewUtils modelAndViewUtils;
    @RequestMapping("/sensor/manager")
    public ModelAndView sensorManager(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/managesensor");

        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/sensor/all")
    public Page listSensors(@PageableDefault(page=1,size=10) Pageable pageable ){

        Page<BileiqiSensor> bileiqiSensors=iBileiqiService.findAll(pageable);
        return bileiqiSensors;
    }
    @RequestMapping("/sensor/chart")
    public ModelAndView sensorChart(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/sensorchart");

        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/sensor/category")
    public List<BileiqiSensor> category(){
        List<BileiqiSensor> bileiqiSensors=iBileiqiService.findAll();
         return bileiqiSensors;
    }
}
