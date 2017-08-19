package com.mdata.thirdparty.dianli.frontend.config;


import com.mdata.thirdparty.dianli.frontend.config.thymeleaf.ResourcesHrefDialect;
import com.mdata.thirdparty.dianli.frontend.config.thymeleaf.ResourcesSrcDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by administrator on 17/7/17.
 */
@Configuration

public class ThymeleafConfiguration {
    public  static final long startTime=System.currentTimeMillis();
    @Bean
    public ResourcesSrcDialect resourcesSrcDialect(){
        return new ResourcesSrcDialect(startTime);
    }
    @Bean
    public ResourcesHrefDialect resourcesHrefDialect(){
        return new ResourcesHrefDialect(startTime);
    }

}
