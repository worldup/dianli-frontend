package com.mdata.thirdparty.dianli.frontend.util;


import com.mdata.thirdparty.dianli.frontend.web.model.base.Menu;

import java.util.List;

/**
 * Created by administrator on 17/7/20.
 */
public class TreeUtils {
    public static Menu transMenuTree(List<Menu> menus, Integer rootId){
        if(menus==null)
            return null;
        Menu rootMenuNode=new Menu();
        rootMenuNode.setId(rootId);
        tranMenuNode(menus,rootMenuNode);
        return rootMenuNode;
    }
    private static void tranMenuNode(List<Menu> menus ,Menu parentNode){
         for(Menu menu:menus){
             if(menu.getParentId().equals(parentNode.getId())){
                 List<Menu> childrens=parentNode.getChildren();

                 childrens.add(menu);

                 tranMenuNode(menus,menu);
             }
         }
    }
    public static void main(String[] args) {

    }
}
