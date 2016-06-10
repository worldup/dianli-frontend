package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
public interface SensorService {
      void testConnect();
      List<Map<String,Object>> list(String pkey);
      List<Map<String,Object>>  getDataBetweenTimeRange(String sid, long beginTime, long endTime);
      List<Map<String,Object>>  getKData(String sid, String idx);
      List<Map<String,Object>>  getThreePhaseData(String aSid,String bSid,String cSid);
      List<Map<String,Object>> getTempHumData(String tSid,String hSid);
}
