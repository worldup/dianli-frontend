package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import com.mdata.thirdparty.dianli.frontend.web.services.system.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/7/31.
 */
@Controller
@RequestMapping("/layout")
public class LayoutController {
  @Autowired
    ModelAndViewUtils modelAndViewUtils;

    @RequestMapping("/")
    public ModelAndView layout(HttpSession session){
        Integer tenantId=(Integer)session.getAttribute("tenantId");
        ModelAndView modelAndView=modelAndViewUtils.newInstance(tenantId);
        modelAndView.setViewName("layout");
        return modelAndView;
    }
    @RequestMapping("/test")
    public ModelAndView test(HttpSession session){
        Integer tenantId=(Integer)session.getAttribute("tenantId");
        ModelAndView modelAndView=modelAndViewUtils.newInstance(tenantId);
        modelAndView.setViewName("/layouttest");
        return modelAndView;
    }
}
