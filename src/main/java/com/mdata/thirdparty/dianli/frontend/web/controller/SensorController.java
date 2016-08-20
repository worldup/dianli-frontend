package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.apache.catalina.manager.util.SessionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by administrator on 16/5/15.
 */
@Controller
@RequestMapping(value = "/sensor",method = {RequestMethod.GET})
public class SensorController {
    @Autowired
    private SensorService sensorService;
    @Autowired
    private ModelAndViewUtils modelAndViewUtils;
    @RequestMapping(value="/count")
    public void count(){
     //   sensorService.testConnect();
    }
    @RequestMapping(value="/list")
    @ResponseBody
    public String list(@RequestParam  String id){
        if(StringUtils.isBlank(id)||"#".equals(id)){
            id="-1";
        }
        StringBuilder sb=new StringBuilder();
        List<Map<String,Object>> result=sensorService.list(id);
        sb.append("<ul>");
        if(!CollectionUtils.isEmpty(result)){

            for(Map<String,Object> map:result){
                sb.append("<li class=\"jstree-closed\" id=\"");
                sb.append(map.get("key"));
                sb.append("\">").append(map.get("name"));
                sb.append("</li>");
            }

        }
        sb.append("</ul>");
        return sb.toString();

    }
    @RequestMapping("/data/tables")
    public ModelAndView tables(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        Map<String,Map<String,Object>>  result=sensorService.getSensorDays("2016-01-06");
        modelAndView.getModel().put("sensors",result.values());
        modelAndView.setViewName( "/data/tables");
        return modelAndView;
    }
    @RequestMapping("/chart/temperature")
    public String temperature(@RequestParam String sid,Map<String,Object> model){
        model.put("sid",sid);
        String sName="此传感器不存在";
        Map<String,Object> sMap= sensorService.getSensorInfo(sid);
        if(sMap!=null){
            sName= MapUtils.getString(sMap,"name","此传感器不存在");
        }
        model.put("sName",sName);
        return "/chart/temperature";
    }
    @RequestMapping("/chart/temperaturek")
    public ModelAndView temperaturek(@RequestParam String sid,@RequestParam("sName") String sName,HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        Map<String,Object> model= modelAndView.getModel();
        model.put("sid",sid);
        Map<String,Object> sMap= sensorService.getSensorInfo(sid);
        if(sMap!=null){
            sName= MapUtils.getString(sMap,"name","此传感器不存在");
        }
        model.put("sName",sName);
        String userName=(String)session.getAttribute("userName");
        model.put("sids",sensorService.getTemperatureSids(userName));
        modelAndView.setViewName("/chart/temperaturek");
        return modelAndView;
    }
    @RequestMapping("/chart/threephasek")
    public ModelAndView threephasek(@RequestParam String aSid,@RequestParam String bSid,@RequestParam String cSid,@RequestParam("sName") String sName,HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
       Map<String,Object> model= modelAndView.getModel();

        model.put("aSid",aSid);
        model.put("bSid",bSid);
        model.put("cSid",cSid);
        model.put("sName",sName);
        String userName=(String)session.getAttribute("userName");
        model.put("sids",sensorService.getThreephaseSids(userName));

        modelAndView.setViewName("/chart/threephasek");
        return modelAndView;
    }
    @RequestMapping("/chart/threephase")
    public String threephase(@RequestParam String aSid,@RequestParam String bSid,@RequestParam String cSid,Map<String, Object> model){
        model.put("aSid",aSid);
        model.put("bSid",bSid);
        model.put("cSid",cSid);
        model.put("sName","名称需要配置");
        return "/chart/threephase";
    }
    @RequestMapping("/chart/humidity")
    public String humidity(@RequestParam String tSid,@RequestParam String hSid,Map<String, Object> model){
        model.put("tSid",tSid);
        model.put("hSid",hSid);
        model.put("sName","名称需要配置");
        return "/chart/humidity";
    }
    @RequestMapping("/chart/humidityk")
    public ModelAndView humidityk(@RequestParam String tSid,@RequestParam String hSid,String tSid1,String hSid1,@RequestParam("sName") String sName,HttpSession session) {
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        Map<String,Object> model= modelAndView.getModel();
        model.put("tSid",tSid);
        model.put("hSid",hSid);
        model.put("tSid1",tSid1);
        model.put("hSid1",hSid1);
        model.put("sName",sName);
        modelAndView.setViewName("/chart/humidityk");
        return modelAndView;
    }
    @RequestMapping("/data/calendar")
    public String calendar(){
        return "/data/calendar";
    }
    @RequestMapping("/data/list")
    @ResponseBody
    public List<Map<String,Object>> listData(String sid, Long start, Long end ){
    List<Map<String,Object>>    result=  sensorService.getDataBetweenTimeRange(sid,start,end);


        return result ;
    }
    @RequestMapping("/kdata/list")
    @ResponseBody
    public List<Map<String,Object>> listKData(  String sid,String idx){
        List<Map<String,Object>>    result=  sensorService.getKData(sid,idx);
        return result ;
    }
    @RequestMapping("/data/listdaydata")
    @ResponseBody
    public List<Map<String,Object>> listDayData(  String sid,String idx,long date){
        String days= FastDateFormat.getInstance("yyyy-MM-dd").format(new Date(date));
        List<Map<String,Object>>    result=  sensorService.getData(sid,idx,days);
        return result ;
    }
    @RequestMapping("/threephase/list")
    @ResponseBody
    public List<Map<String,Object>> threephase(  String aSid,String bSid,String cSid) {
        List<Map<String, Object>> result = sensorService.getThreePhaseData(aSid, bSid, cSid);
        return result;
    }
    @RequestMapping("/threephasek/list")
    @ResponseBody
    public List<Map<String,Object>> threephasek(  String aSid,String bSid,String cSid) {
        List<Map<String, Object>> result = sensorService.getThreePhaseData(aSid, bSid, cSid);
        return result;
    }
    @RequestMapping("/temphumk/list")
    @ResponseBody
    public Map<String,List<Map<String,Object>>> temphumk(  String tSid,String hSid,String tSid1,String hSid1) {
        Map<String,List<Map<String, Object>> >result = sensorService.getTempHumData(tSid, hSid,tSid1,hSid1);
        return result;
    }
    @RequestMapping("/map/jinshan")
    public String jinshan(String tSid, String hSid, Model model) {
        int tenantId=1;
        List<Corporate> corporates=sensorService.getAllCorporate(tenantId);

        ObjectMapper mapper = new ObjectMapper();

        // Convert object to JSON string
        try {
            model.addAttribute("corporates",mapper.writeValueAsString(corporates));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "/amap/jinshan";
    }


}
