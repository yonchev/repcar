package com.repcar.userdata.controllers;

import static com.repcar.userdata.config.UnitTestContext.APP_NAME;
import static com.repcar.userdata.config.UnitTestContext.CATEGORY_RECOMMENDER_URL;
import static com.repcar.userdata.config.UnitTestContext.COMPANY_ID;
import static com.repcar.userdata.config.UnitTestContext.DETAILS_ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.repcar.userdata.Util;
import com.repcar.userdata.config.UnitTestContext;
import com.repcar.userdata.assemblers.RecommendationDetailsAssembler;
import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.repository.RecommendationDetailsRepository;
import com.repcar.userdata.rest.RecommendationDetailsController;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { RecommendationDetailsController.class })
@ContextConfiguration(classes = { RecommendationDetailsController.class, RecommendationDetailsAssembler.class,
        UnitTestContext.class })
public class RecommendationDetailsControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private RecommendationDetailsRepository detailsRepository;

    @MockBean
    private RecommendationDetailsAssembler detailsAssembler;

    @Autowired
    private RecommendationDetails details;

    @Autowired
    private RecommendationDetails createDetails;

    @Autowired
    private RecommendationDetails forUpdateDetails;

    @Before
    public void init() {
        mockMvc = webAppContextSetup(wac).apply(springSecurity(new Filter() {
            @Override
            public void init(FilterConfig arg0) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
                    throws IOException, ServletException {
                // mock OAuth2
                arg2.doFilter(arg0, arg1);
            }

            @Override
            public void destroy() {
            }
        })).build();
    }

    @Test
    public void testGetDetailsByCompnanyId() throws Exception {
        given(detailsRepository.findByCompanyId(COMPANY_ID)).willReturn(details);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", COMPANY_ID.toString());
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetDetailsByCompnanyIdBadRequest() throws Exception {
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("compId", COMPANY_ID.toString());
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDetailsByCompnanyIdNotFound() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(detailsRepository).findByCompanyId(COMPANY_ID);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", COMPANY_ID.toString());
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetDetailsByAppName() throws Exception {
        given(detailsRepository.findByApplicationName(APP_NAME)).willReturn(details);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("applicationName", APP_NAME);
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetDetailsByAppNameNotFound() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(detailsRepository).findByApplicationName(APP_NAME);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("applicationName", APP_NAME);
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetDetailsByAppNameBadRequest() throws Exception {

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("appName", APP_NAME);
        ResultActions result = mockMvc.perform(get("/recommendationdetails").params(mapOfParams)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDetailsByDetailsId() throws Exception {
        given(detailsRepository.findOne(DETAILS_ID)).willReturn(details);

        ResultActions result = mockMvc.perform(get("/recommendationdetails/{recommendationDetailsId}", DETAILS_ID)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetDetailsByDetailsNotFound() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(detailsRepository).findOne(DETAILS_ID);

        ResultActions result = mockMvc.perform(get("/recommendationdetails/{recommendationDetailsId}", DETAILS_ID)
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetDetailsByDetailsIdNotAcceptable() throws Exception {
        mockMvc.perform(get("/recommendationdetails/" + DETAILS_ID).accept(APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testCreateDetails() throws Exception {
        given(detailsRepository.saveAndFlush(createDetails)).willReturn(details);
        given(detailsAssembler.toResource(details)).willCallRealMethod();
        mockMvc.perform(post("/recommendationdetails").contentType(MediaTypes.HAL_JSON_VALUE)
                .content(Util.convertObjectToJsonString(createDetails))).andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE)).andExpect(
                        jsonPath("$._links.self.href").value("http://localhost/recommendationdetails/" + DETAILS_ID));
    }

    @Test
    public void testCreateDetailsBadRequest() throws Exception {
        RecommendationDetails badDetails = new RecommendationDetails();
        details.setCompanyId(2L);
        mockMvc.perform(post("/recommendationdetails").contentType(MediaTypes.HAL_JSON_VALUE)
                .content(Util.convertObjectToJsonString(badDetails))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateDetailsBadRequestWrongUrl() throws Exception {
        RecommendationDetails badDetails = new RecommendationDetails();
        details.setProductRecommenderUrl("abcabc");
        mockMvc.perform(post("/recommendationdetails").contentType(MediaTypes.HAL_JSON_VALUE)
                .content(Util.convertObjectToJsonString(badDetails))).andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateDetailsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/recommendationdetails").content(Util.convertObjectToJsonString(details)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testUpdateDetails() throws Exception {
        RecommendationDetails updateDetails = new RecommendationDetails();
        updateDetails.setRecommendationDetailsId(DETAILS_ID);
        updateDetails.setCategoryRecommenderUrl(CATEGORY_RECOMMENDER_URL);
        updateDetails.setCompanyId(COMPANY_ID);

        given(detailsRepository.findOne(DETAILS_ID)).willReturn(forUpdateDetails);
        given(detailsRepository.saveAndFlush(updateDetails)).willReturn(details);

        mockMvc.perform(put("/recommendationdetails").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(updateDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateDetailsBadRequest() throws Exception {
        RecommendationDetails badDetails = new RecommendationDetails();

        mockMvc.perform(put("/recommendationdetails").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(badDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateDetailsNotFound() throws Exception {
        RecommendationDetails badDetails = new RecommendationDetails();
        badDetails.setRecommendationDetailsId(DETAILS_ID);

        doThrow(new EmptyResultDataAccessException(1)).when(detailsRepository).findOne(DETAILS_ID);
        mockMvc.perform(put("/recommendationdetails").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(badDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteDetails() throws Exception {
        mockMvc.perform(delete("/recommendationdetails/{recommendationDetailsId}", DETAILS_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteDetailsNotFound() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(detailsRepository).delete(DETAILS_ID);
        mockMvc.perform(delete("/recommendationdetails/{recommendationDetailsId}", DETAILS_ID))
                .andExpect(status().isNotFound());
    }
}
