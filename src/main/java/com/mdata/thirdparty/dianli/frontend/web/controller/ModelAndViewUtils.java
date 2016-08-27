package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.beans.TenantLayout;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/8/6.
 */
@Service
public class ModelAndViewUtils {
    @Autowired
    private IMenuService menuService;
    public ModelAndView newInstance(HttpSession session){

        Integer tenantId=(Integer)session.getAttribute("tenantId");
        String userName=(String)session.getAttribute("userName");
        return newInstance(tenantId, userName);
    }
    private   ModelAndView newInstance(int tenantId,String userName){
        ModelAndView modelAndView=new ModelAndView();
        Map<String,Object> model=modelAndView.getModel();
        List<Menu> menus=menuService.listAllMenu(tenantId,userName);
        TenantLayout tenantLayout=menuService.getTenantLayoutById(tenantId);
        Integer warningCount=menuService.getSensorWarningCount();
        model.put("menus",menus);
        model.put("warningCount",warningCount);
         String     layoutTitle=tenantLayout.getTitle();
        String   strongHeader=tenantLayout.getStrongHeader();
        String    smallHeader=tenantLayout.getSmallHeader();
        String layoutFoot=tenantLayout.getFoot();
        model.put("layoutTitle",layoutTitle);
        model.put("strongHeader",strongHeader);
        model.put("smallHeader",smallHeader);
        model.put("layoutFoot",layoutFoot);
        return modelAndView;
    }
}
