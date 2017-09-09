package com.mdata.thirdparty.dianli.frontend.cron;

import com.mdata.thirdparty.dianli.frontend.NettyStarter;
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
    @Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void statusCheck() {
        System.out.println  ("每1分钟执行一次。开始……");
        //statusTask.healthCheck();
        nettyStarter.reconnect();
        System.out.println ("每1分钟执行一次。结束。");
    }
}
