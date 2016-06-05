package com.mdata.thirdparty.dianli.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by administrator on 16/5/14.
 */
@SpringBootApplication

public class Application extends WebMvcConfigurerAdapter {


    @Bean
    public ApplicationSecurity applicationSecurity() {
        return new ApplicationSecurity();
    }

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class).run(args);
    }

    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/assets*//**").permitAll().anyRequest()
                    .fullyAuthenticated().and().formLogin().loginPage("/login")
                    .failureUrl("/login?error").permitAll().and().logout().permitAll();
        }

        //        @Override
//        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.jdbcAuthentication().dataSource(this.dataSource);
//
//        }
        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("admin")
                    .roles("ADMIN", "USER").and().withUser("user").password("user")
                    .roles("USER");
        }

    }

}