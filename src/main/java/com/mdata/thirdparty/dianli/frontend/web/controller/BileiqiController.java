package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.google.gson.Gson;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import com.mdata.thirdparty.dianli.frontend.web.model.PageResult;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.IBileiqiService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    @RequestMapping("/sensormapping/manager")
    public ModelAndView sensorMappingManager(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/managesensormapping");

        return modelAndView;
    }
    @RequestMapping("/sensor/dayk")
    public ModelAndView sensorDayManager(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/sensorday");
        modelAndView.getModel().put("sensorMapping",sensorMapping());
        return modelAndView;
    }
    @RequestMapping("/sensor/weekk")
    public ModelAndView sensorWeekManager(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/sensorweek");
        modelAndView.getModel().put("sensorMapping",sensorMapping());
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping(value = "/sensor/dayk/data",method = {RequestMethod.POST,RequestMethod.GET})
    //mId mappingId
    public Map<String,List> dayKData(@RequestBody Map map ){
        String blWd= MapUtils.getString(map,"blWd");
        String blLj= MapUtils.getString(map,"blLj");
        String blDl= MapUtils.getString(map,"blDl");
        String tqSd= MapUtils.getString(map,"tqSd");
        String tqWd= MapUtils.getString(map,"tqWd");
        String day= MapUtils.getString(map,"day");
       return  iBileiqiService.getDaykData(blWd,blLj,blDl,tqWd,tqSd,day);
    }
    @ResponseBody
    @RequestMapping(value = "/sensor/weekk/data",method = {RequestMethod.POST,RequestMethod.GET})
    //mId mappingId
    public Map<String,List> weekKData(@RequestBody Map map ){
        String blWd= MapUtils.getString(map,"blWd");
        String blLj= MapUtils.getString(map,"blLj");
        String blDl= MapUtils.getString(map,"blDl");
        String tqSd= MapUtils.getString(map,"tqSd");
        String tqWd= MapUtils.getString(map,"tqWd");
        String day= MapUtils.getString(map,"day");
        return  iBileiqiService.getWeekkData(blWd,blLj,blDl,tqWd,tqSd,day);
    }
    private List<BileiqiSensorMapping> sensorMapping(){
        return iBileiqiService.findAllBileiqiMapping();

    }
    @ResponseBody
    @RequestMapping("/sensor/all")
    public Page listSensors(@PageableDefault(page=1,size=10) Pageable pageable ){

        Page<BileiqiSensor> bileiqiSensors=iBileiqiService.findAll(pageable);
        return bileiqiSensors;
    }
    @ResponseBody
    @RequestMapping("/sensormapping/all")
    public Page listSensorMappings(@PageableDefault(page=1,size=10) Pageable pageable ,String pole){

        Page<BileiqiSensorMapping> bileiqiSensors=iBileiqiService.findAllBileiqiMappingByPageAndPole(pageable,pole);
        return bileiqiSensors;
    }
    @ResponseBody
    @RequestMapping(value="/sensormapping",method={RequestMethod.POST},produces="application/json;charset=UTF-8")
    public void addSensorMapping(@RequestBody BileiqiSensorMapping mapping){
        iBileiqiService.addBileiqiMapping(mapping);
    }
    @ResponseBody
    @RequestMapping(value="/sensormapping",method={RequestMethod.DELETE},produces="application/json;charset=UTF-8")
    public void deleteSensorMapping(@RequestBody List<Integer> ids){
        iBileiqiService.deleteBileiqiMapping(ids);
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
