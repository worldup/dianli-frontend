package com.mdata.thirdparty.dianli.frontend.web.repository;


import com.mdata.thirdparty.dianli.frontend.web.model.base.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by administrator on 17/7/20.
 */
public interface MenuRepository extends CrudRepository<Menu,Integer> {
    List<Menu>  findByTenantId(Integer tenantId);
}
