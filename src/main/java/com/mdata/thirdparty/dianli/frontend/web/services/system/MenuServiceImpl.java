package com.mdata.thirdparty.dianli.frontend.web.services.system;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.beans.TenantLayout;
import com.mdata.thirdparty.dianli.frontend.util.TreeUtils;
import com.mdata.thirdparty.dianli.frontend.web.repository.MenuRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */

@Service
public class MenuServiceImpl  implements  IMenuService{


    @Autowired
    private MenuRepository repository;
    @Override
    public List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenus() {
        return Lists.newArrayList(repository.findAll());
    }
    @Override
    public    List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenusByTenantId(Integer tenantId){
        return  Lists.newArrayList(repository.findByTenantId(tenantId));
    }
    @Override
    public  List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenusByUserNameAndTenantId(String userName,Integer tenantId){

        String sql="select distinct m.* from group_members gm ,group_authorities ga,t_menu  m\n" +
                "where gm.group_id=ga.group_id and m.`resource_id`=ga.authority\n" +
                "and gm.username=? and m.tenant_id=?";
        final  List<Menu> menus= jdbcTemplate.query(sql,new Object[]{userName,tenantId},new BeanPropertyRowMapper<Menu>(Menu.class));
        return Lists.transform(menus, new Function<Menu, com.mdata.thirdparty.dianli.frontend.web.model.base.Menu>() {
            @Override
            public com.mdata.thirdparty.dianli.frontend.web.model.base.Menu apply(Menu input) {
                com.mdata.thirdparty.dianli.frontend.web.model.base.Menu menu=new com.mdata.thirdparty.dianli.frontend.web.model.base.Menu();
                menu.setId(Integer.valueOf(input.getId()+""));
                menu.setHref(input.getUrl());
                menu.setParentId(Integer.valueOf(""+input.getPid()));
                menu.setLabel(input.getName());
                menu.setIconClass(input.getIconClass());
                menu.setSort(input.getIdx());
                menu.setTenantId(Integer.valueOf(""+input.getTenantId()));
                menu.setResourceId(input.getResourceId());

                return menu;
            }
        });
    }

    @Override
    public  com.mdata.thirdparty.dianli.frontend.web.model.base.Menu getMenuTreeByTenantId(Integer tenantId){
        List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> menus=getAllMenusByTenantId(tenantId);
        return TreeUtils.transMenuTree(menus,-1);
    }
    @Override
    public com.mdata.thirdparty.dianli.frontend.web.model.base.Menu getMenuTreeByUserNameAndTenantId(String userName,Integer tenantId){
        List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> menus=getAllMenusByUserNameAndTenantId(userName,tenantId);
        return TreeUtils.transMenuTree(menus,-1);
    }

    @Override
    public void addMenu(com.mdata.thirdparty.dianli.frontend.web.model.base.Menu menu) {
        repository.save(menu);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public List<Menu>  listAllMenu(int tenantId){
        String sql="select * from t_menu where tenant_id=?";
       final  List<Menu> menus= jdbcTemplate.query(sql,new Object[]{tenantId},new BeanPropertyRowMapper<Menu>(Menu.class));
      return  FluentIterable.from( Lists.transform(menus, new Function<Menu, Menu>() {
           @Override
           public Menu apply(Menu input) {
               input.setSubMenu(getSubMenus(input,menus));
               return input;
           }
       })).filter(new Predicate<Menu>() {
           @Override
           public boolean apply(Menu input) {
               return input.getPid()==-1;
           }
       }).toList();
    }

    @Override
    public List<Menu> listAllMenu(int tenantId, String userName) {
        String sql="select distinct m.* from group_members gm ,group_authorities ga,t_menu  m\n" +
                "where gm.group_id=ga.group_id and m.`resource_id`=ga.authority\n" +
                "and gm.username=? and m.tenant_id=?";
        final  List<Menu> menus= jdbcTemplate.query(sql,new Object[]{userName,tenantId},new BeanPropertyRowMapper<Menu>(Menu.class));
        return  FluentIterable.from( Lists.transform(menus, new Function<Menu, Menu>() {
            @Override
            public Menu apply(Menu input) {
                input.setSubMenu(getSubMenus(input,menus));
                return input;
            }
        })).filter(new Predicate<Menu>() {
            @Override
            public boolean apply(Menu input) {
                return input.getPid()==-1;
            }
        }).toList();
    }

    private List<Menu> getSubMenus(final Menu menu,List<Menu> allMenu){
       return  FluentIterable.from(allMenu).filter(new Predicate<Menu>() {
            @Override
            public boolean apply( Menu input) {
                return input.getPid()==menu.getId();
            }
        }).toList();
    }

    @Override
    public TenantLayout getTenantLayoutById(int tenantId){
        String sql="select * from t_tenant_layout where id=?";
       List<TenantLayout> tenantLayouts= jdbcTemplate.query(sql,new Object[]{tenantId},new BeanPropertyRowMapper<TenantLayout>(TenantLayout.class));
        if(CollectionUtils.isNotEmpty(tenantLayouts)){
            return tenantLayouts.get(0);
        }
        return new TenantLayout();
    }

//    public Integer getSensorWarningCount(){
//        String sql="select count(1) from t_warning where status=0 and end_time  > DATE_ADD(now(), INTERVAL -2 MONTH)";
//        return jdbcTemplate.queryForObject(sql,new Object[]{},Integer.class);
//    }

}
