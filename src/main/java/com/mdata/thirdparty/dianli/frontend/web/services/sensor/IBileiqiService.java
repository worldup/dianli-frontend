package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by administrator on 17/8/20.
 */
public interface IBileiqiService {
      List<BileiqiSensor> findAll();
      Page<BileiqiSensor> findAll(Pageable pageable);

 }
