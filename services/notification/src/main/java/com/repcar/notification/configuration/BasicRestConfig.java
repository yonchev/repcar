/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */

@Configuration
public class BasicRestConfig {

    @Bean(name = "basicRestTemplate")
    public RestTemplate cmxRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    @Qualifier("basicHeader")
    public HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
