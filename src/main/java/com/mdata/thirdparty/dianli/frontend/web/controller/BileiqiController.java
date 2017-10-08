package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.google.gson.Gson;
import com.mdata.thirdparty.dianli.frontend.forecast.BileiqiForecastService;
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
import java.util.Set;

/**
 * Created by administrator on 17/8/20.
 */
@RestController
@RequestMapping(value = "/bileiqi",method = {RequestMethod.GET})
public class BileiqiController {
    @Autowired
    private IBileiqiService iBileiqiService;
    @Autowired
    private BileiqiForecastService forecastService;
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
    @RequestMapping(value = "/sensor/latest",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> latestMetric(){
        return  iBileiqiService.getSensorLatestMetrics();
    }
    @RequestMapping(value = "/sensor/latestpage",method = RequestMethod.GET)
    public ModelAndView latestPage(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/latestpage");

        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/uncommonsensor",method = RequestMethod.GET)
    public ModelAndView forecastUncommon(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/uncommonsensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/uncommonsensor/data",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> uncommonsensorData(){
       return  forecastService.getUnCommonSensor();
    }
    @RequestMapping(value = "/sensor/forecast/uncalcljsensor",method = RequestMethod.GET)
    public ModelAndView uncalcljsensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/uncalcljsensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/uncalcljsensor/data",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> uncalcljsensorData(){
        return  forecastService.getUnCalcLj();
    }
    @RequestMapping(value = "/sensor/forecast/zerodlsensor",method = RequestMethod.GET)
    public ModelAndView zerodlsensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/zerodlsensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/zerodlsensor/data",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> zerodlsensorData(){
        return  forecastService.getZeroDlSensors();
    }
    @RequestMapping(value = "/sensor/forecast/reepagesensor",method = RequestMethod.GET)
    public ModelAndView reepagesensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/reepagesensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/reepagesensor/data",method = RequestMethod.GET)
    @ResponseBody
    public Set<Map<String,String>> reepagesensorData(){
        return  forecastService.getreepageSensors();
    }
    @RequestMapping(value = "/sensor/forecast/agingsensor",method = RequestMethod.GET)
    public ModelAndView agingsensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/agingsensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/agingsensor/data",method = RequestMethod.GET)
    @ResponseBody
    public Set<Map<String,String>> agingsensorData(){
        return  forecastService.getAgingSensors();
    }
    @RequestMapping(value = "/sensor/forecast/singlephasefailuresensor",method = RequestMethod.GET)
    public ModelAndView singlephasefailuresensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/singlephasefailuresensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/singlephasefailuresensor/data",method = RequestMethod.GET)
    @ResponseBody
    public Set<Map<String,String>> singlephasefailuresensorData(){
        return  forecastService.getSinglePhaseFailure();
    }
    @RequestMapping(value = "/sensor/forecast/multiphasefailuresensor",method = RequestMethod.GET)
    public ModelAndView multiphasefailuresensor(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName( "/bileiqi/forecast/multiphasefailuresensor");
        return modelAndView;
    }
    @RequestMapping(value = "/sensor/forecast/multiphasefailuresensor/data",method = RequestMethod.GET)
    @ResponseBody
    public Set<Map<String,String>> multiphasefailuresensorData(){
        return  forecastService.getMultiPhaseFailure();
    }
}
