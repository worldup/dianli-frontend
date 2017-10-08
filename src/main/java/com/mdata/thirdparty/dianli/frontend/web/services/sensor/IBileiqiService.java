package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 17/8/20.
 */
public interface IBileiqiService {
    List<BileiqiSensor> findAll();

    Page<BileiqiSensor> findAll(Pageable pageable);

    Page<BileiqiSensorMapping> findAllBileiqiMappingByPage(Pageable pageable);

    Page<BileiqiSensorMapping> findAllBileiqiMappingByPageAndPole(Pageable pageable, String pole);

    List<BileiqiSensorMapping> findAllBileiqiMapping();

    Map<String, List> getDaykData(String blWd, String blLj, String blDl, String tqWd, String tqSd, String day);

    Map<String, List> getWeekkData(String blWd, String blLj, String blDl, String tqWd, String tqSd, String day);

    void addBileiqiMapping(BileiqiSensorMapping mapping);

    void deleteBileiqiMapping(List<Integer> ids);

      List<Map<String, Object>> getSensorLatestMetrics();
}
