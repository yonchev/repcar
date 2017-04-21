/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.repcar.notification.beans.Notification;
import com.repcar.notification.beans.Shop;

@Configuration
@EnableSpringDataWebSupport
@ComponentScan("com.repcar.notification.controllers")
public class UnitTestContext {

    public static final String SHOP_NAME = "repcarfinance";
    public static final String SHOP_COUNTRY = "repcarfinance";
    public static final String SHOP_ADDRESS = "repcarfinance";
    public static final String SHOP_CITY = "repcarfinance";
    public static final String SHOP_URL = "repcarfinance";
    public static final String SHOP_TYPE = "repcarfinance";
    public static final Long COMPANY_ID = 2l;
    public static final String COMPANY_ID_STRING = "2";
    public static final Long SHOP_ID = 1l;
    public static final String SHOP_ID_STRING = "1";
    public static final Long NOTIFICATION_ID = 27l;
    public static final String NOTIFICATION_URL = "http://test.com";

    @Bean
    public Notification newNotification() {
        Notification notification = new Notification();
        notification.setCompanyId(COMPANY_ID);
        notification.setShopId(SHOP_ID);
        notification.setNotificationUrl(NOTIFICATION_URL);
        notification.setEventType(Notification.Events.MOVEMENT);
        return notification;
    }

    @Bean
    public Notification persistedNotification() {
        Notification notification = new Notification();
        notification.setNotificationId(NOTIFICATION_ID);
        notification.setNotificationUrl(NOTIFICATION_URL);
        notification.setCompanyId(COMPANY_ID);
        notification.setShopId(SHOP_ID);
        notification.setEventType(Notification.Events.MOVEMENT);
        return notification;
    }

    @Bean
    public Shop persistedShop() {
        Shop shop = new Shop();
        shop.setShopId(SHOP_ID);
        shop.setCompanyId(COMPANY_ID);
        shop.setShopAddress(SHOP_ADDRESS);
        shop.setShopCity(SHOP_CITY);
        shop.setShopCountry(SHOP_COUNTRY);
        shop.setShopName(SHOP_NAME);
        shop.setShopType(SHOP_TYPE);
        shop.setShopUrl(SHOP_URL);
        return shop;
    }

}