package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.apache.catalina.manager.util.SessionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by administrator on 16/5/15.
 */
@Controller
@RequestMapping(value = "/sensor",method = {RequestMethod.GET})
public class SensorController {
    final int perPageSize=10;
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
        String today=DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        Integer sensorCount= sensorService.getSensorDatasByDay(today);
          Integer pageSize=  sensorCount/perPageSize+(sensorCount%perPageSize >0?1:0);
        modelAndView.getModel().put("sensorPageSize",pageSize);
        modelAndView.setViewName( "/data/tables");

        return modelAndView;
    }
    @RequestMapping(value = "/data/sensordata",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,String> sensordata(Integer startPage,@RequestParam(name="sensorName",required = false) String sensorName){
        String day=DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        List<Map<String,Object>> datas=Lists.newArrayList();
        Integer sensorCount=0;
        Integer pageSize=0;
        Map<String,String> result= Maps.newHashMap();
        if(StringUtils.isNotEmpty(sensorName)){
            datas=sensorService.getSensorDatasByDayAndPageAndSName(day,sensorName,startPage,perPageSize);
            sensorCount= sensorService.getSensorDatasByDayAndSName(day,sensorName);
        }
       else {
           datas =sensorService.getSensorDatasByDayAndPage(day,startPage,perPageSize);
             sensorCount= sensorService.getSensorDatasByDay(day);

        }
         pageSize=  sensorCount/perPageSize+(sensorCount%perPageSize >0?1:0);
        final AtomicInteger idx=new AtomicInteger(0);
        List<String> resultList= Lists.transform(datas, new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String, Object> input) {
                StringBuilder sb=new StringBuilder();
                String sname=MapUtils.getString(input,"name");

                sb.append("<tr>");
                sb.append("<td>").append(String.valueOf(idx.addAndGet(1))).append("</td>");

                int sensorIdx=MapUtils.getInteger(input,"idx");
                if(sensorIdx>0){
                    sname+=("("+sensorIdx+")");
                }
                sb.append("<td><span>").append(sname);
                sb.append("</span></td>");
                String status=MapUtils.getString(input,"status");
                sb.append("<td>").append(status.equals("正常")?"正常":"<span class='am-badge am-badge-danger'>异常</span>").append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"sv")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"tmax")).append("</td>");
                sb.append("<td><a href='").append("/sensor/data/realdatapage?sid=").append(MapUtils.getString(input,"sid")).append("&sName=").append(sname).append("&idx=").append(sensorIdx).append("&cvalue=").append(MapUtils.getString(input,"value")).append("'>K线</a></td>");
                sb.append("</tr>");
                return sb.toString();
            }
        });
       result.put("data",Joiner.on("").join(resultList).toString()) ;
       result.put("pageSize",""+pageSize) ;
        return result;
    }
    @RequestMapping("/data/warning")
    public ModelAndView warning(HttpSession session){
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        Integer warningCount= sensorService.getWarningDatasCount();
        Integer pageSize=  warningCount/perPageSize+(warningCount%perPageSize >0?1:0);
        modelAndView.getModel().put("sensorPageSize",pageSize);
        modelAndView.setViewName( "/data/warning");

        return modelAndView;
    }
    @RequestMapping(value = "/data/warningdata",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,String> warningdata(Integer startPage){
        String day=DateFormatUtils.format(new Date(),"yyyy-MM-dd");
        List<Map<String,Object>> datas=Lists.newArrayList();
        Map<String,String> result= Maps.newHashMap();

            datas =sensorService.getWarningDatas(startPage,perPageSize);
            Integer warningCount= sensorService.getWarningDatasCount();

        Integer pageSize=  warningCount/perPageSize+(warningCount%perPageSize >0?1:0);
        final AtomicInteger idx=new AtomicInteger(0);
        List<String> resultList= Lists.transform(datas, new Function<Map<String,Object>, String>() {
            @Override
            public String apply(Map<String, Object> input) {
                StringBuilder sb=new StringBuilder();

                sb.append("<tr>");
                sb.append("<td>").append(String.valueOf(idx.addAndGet(1))).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"name")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"content")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"begin_time")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"end_time")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"count")).append("</td>");
                sb.append("<td>").append(MapUtils.getString(input,"status")).append("</td>");

                sb.append("</tr>");
                return sb.toString();
            }
        });
        result.put("data",Joiner.on("").join(resultList).toString()) ;
        result.put("pageSize",""+pageSize) ;
        return result;
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
    @RequestMapping("/data/realdatapage")
    public ModelAndView realdatapage(@RequestParam String sid,@RequestParam("sName") String sName,@RequestParam (value = "cvalue",required = false) String cvalue, HttpSession session) {
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        Map<String,Object> model= modelAndView.getModel();
        model.put("sid",sid);
        model.put("sName",sName);
        //阈值
            model.put("cvalue",cvalue==null?100:cvalue);

        modelAndView.setViewName("/data/realdata");
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
    @RequestMapping("/data/realdata")
    @ResponseBody
    public Map<String,List<String>> realdata(  String sid,String idx){
        Map<String,List<String>>    result=  sensorService.getRealData(sid,idx);
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
