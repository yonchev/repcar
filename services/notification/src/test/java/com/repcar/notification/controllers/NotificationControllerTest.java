/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.controllers;

import static com.repcar.notification.config.UnitTestContext.COMPANY_ID;
import static com.repcar.notification.config.UnitTestContext.COMPANY_ID_STRING;
import static com.repcar.notification.config.UnitTestContext.NOTIFICATION_ID;
import static com.repcar.notification.config.UnitTestContext.NOTIFICATION_URL;
import static com.repcar.notification.config.UnitTestContext.SHOP_ID;
import static com.repcar.notification.config.UnitTestContext.SHOP_ID_STRING;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.repcar.notification.Util;
import com.repcar.notification.config.UnitTestContext;
import com.repcar.notification.beans.Notification;
import com.repcar.notification.beans.Shop;
import com.repcar.notification.beans.Notification.Events;
import com.repcar.notification.controllers.NotificationController;
import com.repcar.notification.repositories.NotificationRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { NotificationController.class })
@ContextConfiguration(classes = { NotificationController.class, UnitTestContext.class })
public class NotificationControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean(name = "clientRestTemplate")
    private OAuth2RestOperations clientRestTemplate;

    @Autowired
    @Qualifier("newNotification")
    private Notification newNotification;

    @Autowired
    @Qualifier("persistedNotification")
    private Notification persistedNotification;

    @Autowired
    private Shop shop;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(wac).build();
    }

    @Test
    public void testGetNotificationsByCompany() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(persistedNotification);
        given(notificationRepository.findByCompanyIdAndEventType(COMPANY_ID, Notification.Events.MOVEMENT))
                .willReturn(notifications);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", COMPANY_ID_STRING);
        mapOfParams.add("eventType", Events.MOVEMENT.toString());
        this.mockMvc.perform(get("/notifications").params(mapOfParams)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationUrl", is(NOTIFICATION_URL)));
    }

    @Test
    public void testGetNotificationsByShop() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(persistedNotification);
        given(notificationRepository.findByShopIdAndEventType(SHOP_ID, Notification.Events.MOVEMENT))
                .willReturn(notifications);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", SHOP_ID_STRING);
        mapOfParams.add("eventType", Events.MOVEMENT.toString());
        this.mockMvc.perform(get("/notifications").params(mapOfParams)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].notificationUrl", is(NOTIFICATION_URL)));
    }

    @Test
    public void testGetNotificationsBadRequest() throws Exception {
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", SHOP_ID_STRING);
        this.mockMvc.perform(get("/notifications").params(mapOfParams)).andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNotificationsNotFound() throws Exception {
        given(notificationRepository.findByShopIdAndEventType(COMPANY_ID, Notification.Events.MOVEMENT))
                .willReturn(new ArrayList<Notification>());
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", SHOP_ID_STRING);
        mapOfParams.add("eventType", Events.MOVEMENT.toString());
        this.mockMvc.perform(get("/notifications").params(mapOfParams)).andExpect(status().isNotFound());
    }

    @Test
    public void testCreateNotification() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        given(notificationRepository.saveAndFlush(newNotification)).willReturn(persistedNotification);
        this.mockMvc
                .perform(post("/notifications").contentType(HAL_JSON)
                        .content(Util.convertObjectToJsonString(newNotification)))
                .andExpect(status().isCreated()).andExpect(jsonPath("notificationUrl", is(NOTIFICATION_URL)));
    }

    @Test
    public void testCreateNotificationBadRequest1() throws Exception {
        Notification notification = new Notification();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        this.mockMvc.perform(
                post("/notifications").contentType(HAL_JSON).content(Util.convertObjectToJsonString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateNotificationBadRequest2() throws Exception {
        Notification notification = new Notification("abc",Events.MOVEMENT,6L,SHOP_ID);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        this.mockMvc.perform(
                post("/notifications").contentType(HAL_JSON).content(Util.convertObjectToJsonString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteNotification() throws Exception {
        doNothing().when(notificationRepository).delete(NOTIFICATION_ID);
        this.mockMvc.perform(delete("/notifications/{notificationId}", NOTIFICATION_ID).contentType(HAL_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotificationNonExisting() throws Exception {
        doThrow(EmptyResultDataAccessException.class).when(notificationRepository).delete(NOTIFICATION_ID);
        this.mockMvc.perform(delete("/notifications/{notificationId}", NOTIFICATION_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateNotification() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        given(notificationRepository.findOne(persistedNotification.getNotificationId())).willReturn(persistedNotification);
        given(notificationRepository.saveAndFlush(persistedNotification)).willReturn(persistedNotification);
        this.mockMvc
                .perform(put("/notifications").contentType(HAL_JSON)
                        .content(Util.convertObjectToJsonString(persistedNotification)))
                .andExpect(status().isOk()).andExpect(jsonPath("notificationUrl", is(NOTIFICATION_URL)));
    }

    @Test
    public void testUpdateNotificationBadRequest1() throws Exception {
        Notification notification = new Notification();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        this.mockMvc.perform(
                put("/notifications").contentType(HAL_JSON).content(Util.convertObjectToJsonString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateNotificationBadRequest2() throws Exception {
        Notification notification = new Notification("abc",Events.MOVEMENT,6L,SHOP_ID);
        notification.setNotificationId(NOTIFICATION_ID);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        this.mockMvc.perform(
                put("/notifications").contentType(HAL_JSON).content(Util.convertObjectToJsonString(notification)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateNotificationNotFound() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", SHOP_ID);
        given(clientRestTemplate.getForObject("http://category/shops/{shopId}", Shop.class, params)).willReturn(shop);

        given(notificationRepository.findOne(persistedNotification.getNotificationId())).willReturn(new Notification());
        this.mockMvc
                .perform(put("/notifications").contentType(HAL_JSON)
                        .content(Util.convertObjectToJsonString(persistedNotification)))
                .andExpect(status().isOk()).andExpect(jsonPath("notificationUrl", is(NOTIFICATION_URL)));
    }

}