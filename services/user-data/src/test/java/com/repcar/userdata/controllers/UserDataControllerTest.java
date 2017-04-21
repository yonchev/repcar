/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.controllers;

import static com.repcar.userdata.config.UnitTestContext.COMPANY_ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import feign.FeignException;
import feign.Response;
import io.prediction.EngineClient;
import io.prediction.EventClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.repcar.userdata.Util;
import com.repcar.userdata.config.UnitTestContext;
import com.repcar.userdata.bean.EntityType;
import com.repcar.userdata.bean.Event;
import com.repcar.userdata.bean.EventType;
import com.repcar.userdata.bean.RecommendedContent;
import com.repcar.userdata.domain.IdMap;
import com.repcar.userdata.domain.Product;
import com.repcar.userdata.repository.CategoryRepository;
import com.repcar.userdata.repository.IdMapRepository;
import com.repcar.userdata.repository.ProductRepository;
import com.repcar.userdata.repository.RecommendationDetailsRepository;
import com.repcar.userdata.rest.UserDataController;
import com.repcar.userdata.services.ConnectionsService;
import com.repcar.userdata.services.EventServer;
import com.repcar.userdata.services.EventServerConnector;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { UserDataController.class })
@ContextConfiguration(classes = { UserDataController.class, UnitTestContext.class })
public class UserDataControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private EventClient eventClient;

    @MockBean
    private EngineClient engineClient;

    @MockBean
    private EventServerConnector esc;

    @MockBean
    private EventServer es;

    @MockBean
    private ConnectionsService connections;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private IdMapRepository idMapRepository;

    @MockBean
    private RecommendationDetailsRepository recommendationDetailsRepository;

    @Autowired
    private Product product;

    @Autowired
    private IdMap idMap;

    @Autowired
    private Event event;

    @Before
    public void init() {
        mockMvc = webAppContextSetup(wac).apply(springSecurity(new Filter() {
            @Override
            public void init(FilterConfig arg0) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
                    ServletException {
                // mock OAuth2
                arg2.doFilter(arg0, arg1);
            }

            @Override
            public void destroy() {
            }
        })).build();
    }

    @Test
    @Ignore("Because cannot mock productRepository.getForObject()")
    public void testGetRecommendations() throws Exception {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("item", 1);
        jsonArray.add(jsonOb);
        jsonObject.add("itemScores", jsonArray);
        given(esc.getRecommendation(1L, "abc", RecommendedContent.product)).willReturn(jsonObject);
        List<Product> products = new ArrayList<Product>();
        products.add(product);
        given(productRepository.findAllByCompanyIdAndProductIdIn(any(Long.class), any(List.class)))
                .willReturn(products);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        Iterator<Product> iterator = mock(Iterator.class);
        given(products.iterator()).willReturn(iterator);
        given(iterator.hasNext()).willReturn(true, false);
        given(iterator.next()).willReturn(product);
        mapOfParams.add("weakId", "abc");
        mapOfParams.add("companyId", "1");
        mapOfParams.add("count", "1");
        mockMvc.perform(get("/userdata/recommendation").params(mapOfParams)).andExpect(status().isOk());
    }

    @Test
    @Ignore("Because cannot mock productRepository.getForObject()")
    public void testFindByProductId() throws Exception {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonOb = new JsonObject();
        jsonOb.addProperty("item", 1);
        jsonArray.add(jsonOb);
        jsonObject.add("itemScores", jsonArray);
        given(esc.getRecommendation(1L, "abc", RecommendedContent.product)).willReturn(jsonObject);
        List<Product> products = new ArrayList<Product>();
        given(productRepository.findAllByCompanyIdAndProductIdIn(any(Long.class), any(List.class))).willReturn(products);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("weakId", "abc");
        mapOfParams.add("companyId", COMPANY_ID.toString());
        mapOfParams.add("count", "1");
        mockMvc.perform(get("/userdata/recommendation").params(mapOfParams)).andExpect(status().isOk());
    }

    @Test
    public void testGetRecommendationsNoUser() throws Exception {
        given(esc.getRecommendation(1L, "abc", RecommendedContent.product)).willReturn(new JsonObject());
        mockMvc.perform(get("/userdata/recommendation/product")).andExpect(status().isBadRequest());
    }

    @Test
    public void testSetEvent() throws Exception {
        given(productRepository.findOne(Long.valueOf(event.getTargetEntityId().substring(1)))).willReturn(product);
        given(idMapRepository.findByWeakId(event.getEntityId())).willReturn(idMap);
        given(esc.sendEvent(any(Long.class), any(Event.class))).willReturn("fs346DRf");
        mockMvc.perform(
                post("/userdata").accept(HAL_JSON).contentType(HAL_JSON).content(Util.convertObjectToJsonString(event)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSetEventNoBody() throws Exception {
        given(esc.sendEvent(any(Long.class), any(Event.class))).willReturn("fs346DRf");
        mockMvc.perform(post("/userdata").accept(HAL_JSON).contentType(HAL_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testSetEventInvalidBody() throws Exception {
        given(esc.sendEvent(any(Long.class), any(Event.class))).willReturn("fs346DRf");
        mockMvc.perform(post("/userdata").accept(HAL_JSON).contentType(HAL_JSON).content("{\"dummy\":\"view\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Ignore
    public void testGetEventInvalidWeakId() throws Exception {
        String invalidWeakId = "undefined";
        String notFound = "Not Found";
        String messageNotFound = String.format("{\"message\":\"%s\"}", notFound);
        String messageNotFoundEscaped = String.format("{\\\"message\\\":\\\"%s\\\"}", notFound);
        String status = "EventServer#getEvent(String,EventType,String,boolean,int,EntityType,EntityType)";
        String body = "status 404 reading " + status + "; content:\\n" + messageNotFoundEscaped;
        given(esc.getEvent(1L, eq(invalidWeakId))).willCallRealMethod();
        given(
                es.getEvent(eq(invalidWeakId), eq(EventType.view), anyString(), eq(true), eq(1), eq(EntityType.user),
                        eq(EntityType.item))).willThrow(
                new RuntimeException(FeignException.errorStatus(
                        status,
                        Response.create(404, notFound, new HashMap<String, Collection<String>>(),
                                messageNotFound.getBytes()))));
        mockMvc.perform(get("/userdata/{weakId}", invalidWeakId).accept(HAL_JSON)).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(content().json("{'status':404,'message':\"" + body + "\"}"));
    }

    @Test
    @Ignore
    public void testGetEvent() throws Exception {
        String id = "107";
        given(esc.getEvent(1L, eq(id))).willReturn(event);
        given(productRepository.findOne(Long.valueOf(id.substring(1)))).willReturn(product);

        mockMvc.perform(get("/userdata/{weakId}", id).accept(HAL_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(content().json(Util.convertObjectToJsonString(product)));
    }

    @Test
    @Ignore
    public void testGetEventNoProduct() throws Exception {
        String id = "107";
        given(esc.getEvent(1L, eq(id))).willReturn(event);
        given(productRepository.findOne(Long.valueOf(id.substring(1)))).willReturn(null);

        mockMvc.perform(get("/userdata/{weakId}", id).accept(HAL_JSON)).andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    @Ignore
    public void replace() throws Exception {
        String oldWeakId = "101";
        String newWeakId = "abv";
        Long companyId = 1L;
        willDoNothing().given(esc).replace(companyId, oldWeakId, newWeakId);
        mockMvc.perform(post("/userdata/{oldWeakId}/{newWeakId}", oldWeakId, newWeakId)).andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void replaceNonExisting() throws Exception {
        String oldWeakId = "101";
        String newWeakId = "abv";
        Long companyId = 1L;
        String status = "EventServer#getEvents(String,String,int)";
        String notFound = "Not Found";
        String messageNotFound = String.format("{\"message\":\"%s\"}", notFound);
        String messageNotFoundEscaped = String.format("{\\\"message\\\":\\\"%s\\\"}", notFound);
        String body = "status 404 reading " + status + "; content:\\n" + messageNotFoundEscaped;
        willThrow(
                FeignException.errorStatus(
                        status,
                        Response.create(404, notFound, new HashMap<String, Collection<String>>(),
                                messageNotFound.getBytes()))).given(esc).replace(companyId, oldWeakId, newWeakId);
        mockMvc.perform(
                post("/userdata/{oldWeakId}/{newWeakId}", oldWeakId, newWeakId).accept(HAL_JSON).contentType(HAL_JSON))
                .andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(content().json("{'status':404,'message':\"" + body + "\"}"));
    }
}
