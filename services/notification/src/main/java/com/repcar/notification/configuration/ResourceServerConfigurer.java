/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    private ClientHttpRequestFactory clientHttpRequestFactory;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/health", "/info").permitAll()
                .antMatchers("/notifications*", "/notifications/*").hasAnyRole("CLIENT", "ADMIN")
                .anyRequest().authenticated();
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

    @Bean
    @LoadBalanced
    public RestTemplate configurationRestTemplate() {
        return new RestTemplate();
    }

}