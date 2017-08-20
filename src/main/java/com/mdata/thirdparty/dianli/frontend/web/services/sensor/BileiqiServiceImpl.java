package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.google.common.collect.Lists;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.repository.BileiqiSensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 17/8/20.
 */
@Service
public class BileiqiServiceImpl implements IBileiqiService {
    @Autowired
    private BileiqiSensorRepository repository;
    public List<BileiqiSensor> findAll(){
       return Lists.newArrayList(repository.findAll());
    }

    @Override
    public Page<BileiqiSensor> findAll(Pageable pageable) {
       return  repository.findAll(pageable);
    }


}
