package com.mdata.thirdparty.dianli.frontend.web.controller;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */
@RestController
@RequestMapping(value = "/menu",method = {RequestMethod.GET,RequestMethod.POST})
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @RequestMapping(value="/list")
    public List<Menu> list(int tenantId){
        return  menuService.listAllMenu(tenantId);
    }

}
