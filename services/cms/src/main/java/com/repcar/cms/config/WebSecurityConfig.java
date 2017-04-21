/*
 * Copyright RepCar AD 2017
 */
package com.repcar.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO Configure different view permissions for ADMIN, etc.
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("index.html", "/lib/**", "/view/**", "/js/**", "/info", "/collaboration/**", "/images/**",
                        "/images/**", "/user-data/userdata", "/user-data/userdata/**", "/idmap/idMaps",
                        "/idmap/idMaps/**", "/product/products*", "/product/products/**", "/company/companies*",
                        "/company/companies/**", "/category/categories*", "/category/categories/**", "/security/**",
                        "/cmx/macs/**", "/meeting/meetings", "/meeting/meetings/*", "/user/users/**",
                        "/collaboration/jabberSettings", "/collaboration/jabberSettings/*", "/token")
                .permitAll().anyRequest().authenticated().and().logout().logoutSuccessUrl("/").permitAll().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

}