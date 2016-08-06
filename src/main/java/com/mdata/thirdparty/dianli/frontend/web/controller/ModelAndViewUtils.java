package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/8/6.
 */
@Service
public class ModelAndViewUtils {
    @Autowired
    private IMenuService menuService;
    public   ModelAndView newInstance(int tenantId){
        ModelAndView modelAndView=new ModelAndView();
        Map<String,Object> model=modelAndView.getModel();
        List<Menu> menus=menuService.listAllMenu(tenantId);
        model.put("menus",menus);
        String layoutTitle="";
        String strongHeader="";
        String smallHeader="";
        if(tenantId==1)
        {
              layoutTitle="上海金山工业区能效管理服务平台";
              strongHeader="上海金山工业区";
              smallHeader="能效管理服务平台";
        }
        else if(tenantId==2){
              layoutTitle="欧忆智能监测平台";
              strongHeader="欧忆";
              smallHeader="配电站智能监测平台";
        }
        model.put("layoutTitle",layoutTitle);
        model.put("strongHeader",strongHeader);
        model.put("smallHeader",smallHeader);
        return modelAndView;
    }
}
