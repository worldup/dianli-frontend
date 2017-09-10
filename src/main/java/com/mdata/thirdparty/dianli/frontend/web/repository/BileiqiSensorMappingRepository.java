package com.mdata.thirdparty.dianli.frontend.web.repository;

import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BileiqiSensorMappingRepository extends PagingAndSortingRepository<BileiqiSensorMapping,Integer> {
    Page<BileiqiSensorMapping> findAllByPoleLike(String pole, Pageable pageable);
}
