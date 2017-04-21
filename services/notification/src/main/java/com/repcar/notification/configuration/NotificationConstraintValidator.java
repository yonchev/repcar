package com.repcar.notification.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.repcar.notification.beans.Notification;
import com.repcar.notification.beans.Shop;
import com.repcar.notification.controllers.NotificationController;

public class NotificationConstraintValidator implements ConstraintValidator<NotificationValidator, Notification> {

    @Qualifier("clientRestTemplate")
    @Autowired
    private OAuth2RestOperations clientRestTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Override
    public void initialize(NotificationValidator arg0) {
    }

    @Override
    public boolean isValid(Notification notification, ConstraintValidatorContext context) {
        if(notification.getShopId()!=null){
        Shop shop = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", notification.getShopId());
        try {
            shop = clientRestTemplate.getForObject("http://category/shops/{shopId}",
                    Shop.class, params);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getRawStatusCode() == 404) {
                JSONObject responseInJson = new JSONObject(e.getResponseBodyAsString());
                logger.info("Shop with id : {} is not found", notification.getShopId());
                throw new IllegalArgumentException(responseInJson.getString("message"));
            } else {
                JSONObject responseInJson = new JSONObject(e.getResponseBodyAsString());
                logger.info("Error from Shop with message {}", responseInJson.getString("message"));
                throw new HttpServerErrorException(e.getStatusCode(), responseInJson.getString("message"));
            }
        }
        if(!(shop.getCompanyId().equals(notification.getCompanyId()))){
            return false;
        }
        }
        return true;
    }

}
