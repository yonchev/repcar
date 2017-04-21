/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Configuration
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final ClientHttpRequestFactory clientHttpRequestFactory;

    @Autowired
    public ResourceServerConfigurer(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.clientHttpRequestFactory = clientHttpRequestFactory;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/health", "/info").permitAll()
                //Only userdata post + recommendations is allowed to all roles, other are forbidden.
                .antMatchers("/userdata*", "/userdata/*", "/userdata/recommendation/*").permitAll()
                .antMatchers("/recommendationdetails*", "/recommendationdetails/*")
                .hasAnyRole("CLIENT", "ADMIN").anyRequest().authenticated();
    }


    @Bean
    public UserInfoRestTemplateCustomizer loadBalancedUserInfoRestTemplateCustomizer() {
        return new UserInfoRestTemplateCustomizer() {
            @Override
            public void customize(OAuth2RestTemplate restTemplate) {
                restTemplate.setRequestFactory(clientHttpRequestFactory);
            }
        };
    }
}
