/*
 * Copyright RepCar AD 2017
 */
package com.repcar.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Discovery service for WeeShop project.
 *
 * @author <a href="tslavkov@repcarpro.com>Tihomir Slavkov</a>
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class Application extends SpringBootServletInitializer  {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("Starting RepCar discovery...");
    }
}
