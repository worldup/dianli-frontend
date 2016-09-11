package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
@Controller
public class AuthController {
    final int perPageSize=20;
    @Autowired
    private ModelAndViewUtils modelAndViewUtils;
    @Autowired
    private SensorService sensorService;
    @RequestMapping("/home")
    public ModelAndView home( HttpSession session) {

        Integer tenantId=(Integer)session.getAttribute("tenantId");
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);


        if(tenantId==1){
            List<Corporate> corporates=sensorService.getAllCorporate(tenantId);

            ObjectMapper mapper = new ObjectMapper();

            // Convert object to JSON string
            try {
                modelAndView.getModel().put("corporates",mapper.writeValueAsString(corporates));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            modelAndView.setViewName("jshome");
        }
        else if(tenantId==2){
            String today= DateFormatUtils.format(new Date(),"yyyy-MM-dd");
            Integer sensorCount= sensorService.getSensorDatasByDay(today);
            List<Map<String, Object>> sensorTrees=sensorService.listSensorTree();
            Integer pageSize=  sensorCount/perPageSize+(sensorCount%perPageSize >0?1:0);
            modelAndView.getModel().put("sensorPageSize",pageSize);
            modelAndView.getModel().put("sensorTrees",sensorTrees);
            modelAndView.setViewName( "/data/tables");
        }

        return modelAndView;
    }

    @RequestMapping("/foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }
    @RequestMapping("/login")
    public String login() {
       return "login";
    }
    @RequestMapping("/")
    public ModelAndView index(HttpSession session) {
        Integer tenantId=(Integer)session.getAttribute("tenantId");
        ModelAndView modelAndView=modelAndViewUtils.newInstance(session);
        modelAndView.setViewName("welcome");
        return modelAndView;
    }
}
