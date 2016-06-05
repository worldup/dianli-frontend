package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
public interface SensorService {
    public void testConnect();
    public List<Map<String,Object>> list(String pkey);
}
