package com.mdata.thirdparty.dianli.frontend.cron;

import com.mdata.thirdparty.dianli.frontend.NettyStarter;
import com.mdata.thirdparty.dianli.frontend.forecast.BileiqiService;
import com.mdata.thirdparty.dianli.frontend.forecast.BileiqiUnCommonConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by administrator on 17/8/13.
 */
@Component
@Configurable
@EnableScheduling
public class LoraCronService {
    @Autowired
    private NettyStarter nettyStarter;
    @Autowired
    private BileiqiService bileiqiService;
    @Scheduled(cron="0 0/5 * * * ?") //每分钟执行一次
    public void statusCheck() {
        System.out.println  ("每5分钟执行一次。开始……");
        //statusTask.healthCheck();
        nettyStarter.reconnect();
        System.out.println ("每5分钟执行一次。结束。");
    }
    @Scheduled(cron="0/5 * * * * ?")
    public void forecast(){
        bileiqiService.getUnCommonDLSensors();
    }
}
