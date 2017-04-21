/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.repcar.userdata.bean.EntityType;
import com.repcar.userdata.bean.Event;
import com.repcar.userdata.bean.EventType;
import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.domain.IdMap;
import com.repcar.userdata.domain.Product;

@Configuration
@EnableSpringDataWebSupport
public class UnitTestContext {

    public static final Long COMPANY_ID = 87L;
    public static final Long DETAILS_ID = 123L;
    public static final String APP_NAME = "repcarApp";
    public static final String APP_ACCESS_KEY = "repcarAppAccessKey";
    public static final String PRODUCT_RECOMMENDER_URL = "http://1.2.3.4:8001";
    public static final String CATEGORY_RECOMMENDER_URL = "http://1.2.3.4:8002";

    @Bean
    public Event event() {
        Event event = new Event();
        event.setTargetEntityId("i107");
        event.setTargetEntityType(EntityType.item);
        event.setEntityId("1087");
        event.setEntityType(EntityType.user);
        event.setEvent(EventType.view);
        return event;
    }

    @Bean
    public Product createProduct() {
        Product product = new Product();
        product.setProductId(600L);
        product.setProductName("Topka");
        product.setProductPrice("2.6");
        product.setProductImage("car.jpg");
        product.setProductCategory(null);
        product.setProductDescription("abc");
        product.setCompanyId(COMPANY_ID);
        return product;
    }

    @Bean
    public IdMap idmap() {
        return new IdMap(118L, 12L, "abc", null, "test", true, COMPANY_ID, "");
    }

    @Bean
    public RecommendationDetails details() {
        RecommendationDetails details = new RecommendationDetails();
        details.setRecommendationDetailsId(DETAILS_ID);
        details.setApplicationName(APP_NAME);
        details.setApplicationAccessKey(APP_ACCESS_KEY);
        details.setProductRecommenderUrl(PRODUCT_RECOMMENDER_URL);
        details.setCategoryRecommenderUrl(CATEGORY_RECOMMENDER_URL);
        details.setCompanyId(COMPANY_ID);
        return details;
    }

    @Bean
    public RecommendationDetails createDetails() {
        RecommendationDetails details = new RecommendationDetails();
        details.setApplicationName(APP_NAME);
        details.setApplicationAccessKey(APP_ACCESS_KEY);
        details.setProductRecommenderUrl(PRODUCT_RECOMMENDER_URL);
        details.setCategoryRecommenderUrl(CATEGORY_RECOMMENDER_URL);
        details.setCompanyId(COMPANY_ID);
        return details;
    }

    @Bean
    public RecommendationDetails forUpdateDetails() {
        RecommendationDetails details = new RecommendationDetails();
        details.setRecommendationDetailsId(DETAILS_ID);
        details.setApplicationName(APP_NAME);
        details.setApplicationAccessKey(APP_ACCESS_KEY);
        details.setProductRecommenderUrl(PRODUCT_RECOMMENDER_URL);
        details.setCompanyId(COMPANY_ID);
        return details;
    }
}