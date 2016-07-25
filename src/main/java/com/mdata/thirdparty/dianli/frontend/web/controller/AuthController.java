package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.sensor.SensorService;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
@Controller
public class AuthController {
    @Autowired
    private IMenuService menuService;
    @Autowired
    private SensorService sensorService;
    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        model.put("message", "Hello World");
        model.put("title", "Hello Home");
        model.put("date", new Date());
        List<Menu> menus=menuService.listAllMenu(1);
        model.put("menus",menus);
        int tenantId=1;
        List<Corporate> corporates=sensorService.getAllCorporate(tenantId);

        ObjectMapper mapper = new ObjectMapper();

        // Convert object to JSON string
        try {
            model.put("corporates",mapper.writeValueAsString(corporates));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "jshome";
    }

    @RequestMapping("/foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }
    @RequestMapping("/login")
    public String login() {
       return "login";
    }
}
