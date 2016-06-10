package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @RequestMapping("/chart/temperature")
    public String temperature(){
        return "/chart/temperature";
    }
    @RequestMapping("/chart/temperaturek")
    public String temperaturek(){
        return "/chart/temperaturek";
    }
    @RequestMapping("/chart/threephasek")
    public String threephasek(){
        return "/chart/threephasek";
    }
    @RequestMapping("/chart/threephase")
    public String threephase(){
        return "/chart/threephase";
    }
    @RequestMapping("/chart/humidity")
    public String humidity(){
        return "/chart/humidity";
    }
    @RequestMapping("/chart/humidityk")
    public String humidityk(){
        return "/chart/humidityk";
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
    public List<Map<String,Object>> temphumk(  String tSid,String hSid) {
        List<Map<String, Object>> result = sensorService.getTempHumData(tSid, hSid);
        return result;
    }
}
