/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    private final ClientHttpRequestFactory clientHttpRequestFactory;

    @Autowired public ResourceServerConfigurer(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.clientHttpRequestFactory = clientHttpRequestFactory;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/health", "/info", "/products*", "/products/*")
                .permitAll().antMatchers("/products*", "/products/*").hasAnyRole("CLIENT", "ADMIN", "OPERATOR")
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

    @Bean(name = "restTemplate")
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(clientHttpRequestFactory);
        return template;
    }
}