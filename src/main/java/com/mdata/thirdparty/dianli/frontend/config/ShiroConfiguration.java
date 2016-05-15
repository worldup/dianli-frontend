package com.mdata.thirdparty.dianli.frontend.config;

import com.mdata.thirdparty.dianli.frontend.web.services.shiro.SqliteDBRealm;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 * Created by administrator on 16/5/15.
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
        filterProxy.setTargetBeanName("shiroFilter");
        filterProxy.setTargetFilterLifecycle(true);
        registration.setFilter(filterProxy);
        registration.addUrlPatterns("/");
        registration.setOrder(1);
        return registration;
    }

    @Bean(name = "shiroFilter")
    @Autowired
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl("/login");
        factoryBean.setUnauthorizedUrl("/login");
        return factoryBean;
    }

    @Bean(name = "securityManager")
    @Autowired
    public DefaultWebSecurityManager securityManager(SqliteDBRealm authorizingRealm) {
        final DefaultWebSecurityManager securityManager
                = new DefaultWebSecurityManager();
        authorizingRealm.setCredentialsMatcher(credentialsMatcher());
        securityManager.setRealm(authorizingRealm);
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public ServletContainerSessionManager sessionManager() {
        final ServletContainerSessionManager sessionManager
                = new ServletContainerSessionManager();

        return sessionManager;
    }



    @Bean(name = "credentialsMatcher")
    public PasswordMatcher credentialsMatcher() {
        final PasswordMatcher credentialsMatcher = new PasswordMatcher();
        credentialsMatcher.setPasswordService(passwordService());
        return credentialsMatcher;
    }

    @Bean(name = "passwordService")
    public DefaultPasswordService passwordService() {
        return new DefaultPasswordService();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoproxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @Autowired
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}