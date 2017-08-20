package com.mdata.thirdparty.dianli.frontend.web.repository;

import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by administrator on 17/8/20.
 */

public interface BileiqiSensorRepository extends PagingAndSortingRepository<BileiqiSensor,String> {
}
