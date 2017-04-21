/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata;

import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author <a href="mailto:tslavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableResourceServer
@EnableFeignClients
@EnableOAuth2Client
@EnableHypermediaSupport(type = { HypermediaType.HAL })
public class Application extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        // Configure Netty (which is used by async-http-client, which is used by PredictionIO's client) to use Slf4J for
        // logging
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        SpringApplication.run(Application.class, args);
        logger.info("Started user-data");
    }

}
