package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import com.mdata.thirdparty.dianli.frontend.web.repository.BileiqiSensorMappingRepository;
import com.mdata.thirdparty.dianli.frontend.web.repository.BileiqiSensorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 17/8/20.
 */
@Service
public class BileiqiServiceImpl implements IBileiqiService {
    @Autowired
    private BileiqiSensorRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BileiqiSensorMappingRepository mappingRepository;
    public List<BileiqiSensor> findAll(){
       return Lists.newArrayList(repository.findAll());
    }

    @Override
    public Page<BileiqiSensor> findAll(Pageable pageable) {
       return  repository.findAll(pageable);
    }
 @Override
  public Page<BileiqiSensorMapping> findAllBileiqiMappingByPage(Pageable pageable){
        return mappingRepository.findAll(pageable);
 }
    @Override
    public Page<BileiqiSensorMapping> findAllBileiqiMappingByPageAndPole(Pageable pageable,String pole){
      if(StringUtils.isNotEmpty(pole)){
          return mappingRepository.findAllByPoleLike("%"+pole+"%",pageable);
      }
      else{
          return mappingRepository.findAll(pageable);
      }

    }

    @Override
    public List<BileiqiSensorMapping> findAllBileiqiMapping() {
        return Lists.newArrayList(mappingRepository.findAll());
    }

    @Override
    public Map<String, List> getDaykData(final String blWd, final String blLj, final String blDl, final String tqWd, final String tqSd, String day) {
       //显示分钟级别统计
        String sql="select sid,max(sv) sv, date_format(tmin,'%H:%i') t  from t_sensor_data where days =? and sid in(?,?,?,?,?) group by sid,date_format(tmin,'%H:%i')" ;


         List <Map<String,Object>> result=jdbcTemplate.query(sql,new Object[]{day,blWd,blLj,blDl,tqWd,tqSd},new ColumnMapRowMapper());
        Map<String,List> map=list2Map(blWd,blLj,blDl,tqWd,tqSd,result);
        return map;
    }
    @Override
    public Map<String, List> getWeekkData(final String blWd, final String blLj, final String blDl, final String tqWd, final String tqSd, String day) {
        //显示分钟级别统计
        String sql="select sid,max(sv) sv, date_format(tmin,'%Y%m%d%H%i') t  from t_sensor_data where days >=date_add(?,interval -6 day) and sid in(?,?,?,?,?) group by sid,date_format(tmin,'%Y%m%d%H%i')" ;


        List <Map<String,Object>> result=jdbcTemplate.query(sql,new Object[]{day,blWd,blLj,blDl,tqWd,tqSd},new ColumnMapRowMapper());
        Map<String,List> map=list2Map(blWd,blLj,blDl,tqWd,tqSd,result);
        return map;
    }
    private Map<String, List> list2Map(final String blWd, final String blLj, final String blDl, final String tqWd, final String tqSd,List <Map<String,Object>> result){
        Map<String,List> map=Maps.newHashMap();
        List blWdLst=filterBySid(blWd,result);
        List blLjLst=filterBySid(blLj,result);
        List blDlLst=filterBySid(blDl,result);
        List tqWdLst=filterBySid(tqWd,result);
        List tqSdLst=filterBySid(tqSd,result);
        map.put("blWd",blWdLst);
        map.put("blLj",blLjLst);
        map.put("blDl",blDlLst);
        map.put("tqWd",tqWdLst);
        map.put("tqSd",tqSdLst);
        return map;
    }
    private List filterBySid(final String sid,List<Map<String,Object>> list){
        List result=Lists.newArrayList(Iterables.filter(list, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> input) {
                return  sid.equals(input.get("sid"));
            }
        }));
        return result;
    }
    public void addBileiqiMapping(BileiqiSensorMapping mapping) {
        BileiqiSensorMapping result=  mappingRepository.save(mapping);
    }

    public void deleteBileiqiMapping(List<Integer> ids){
        Iterable<BileiqiSensorMapping> mappingList=  Iterables.transform(ids, new Function<Integer, BileiqiSensorMapping>() {
            @Override
            public BileiqiSensorMapping apply(Integer input) {
                BileiqiSensorMapping mapping=new BileiqiSensorMapping();
                mapping.setId(input);
                return mapping;
            }
        });
        mappingRepository.delete(mappingList);
    }
}
