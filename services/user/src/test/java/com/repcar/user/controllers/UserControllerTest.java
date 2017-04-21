/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.controllers;

import static com.repcar.user.Util.convertObjectToJsonString;
import static com.repcar.user.config.UnitTestContext.COMPANY_ID;
import static com.repcar.user.config.UnitTestContext.USER_ID;
import static com.repcar.user.config.UnitTestContext.USER_PASSWORD_ENCRYPTED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.repcar.user.Util;
import com.repcar.user.assembler.UserAssembler;
import com.repcar.user.beans.User;
import com.repcar.user.config.UnitTestContext;
import com.repcar.user.controllers.UserController;
import com.repcar.user.encryption.EncryptDecryptService;
import com.repcar.user.repositories.UserRepository;
import com.repcar.user.resources.UserResource;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { UserController.class })
@ContextConfiguration(classes = { UserController.class, UserAssembler.class, UnitTestContext.class,
        UserControllerTest.ResourceServerContext.class, EncryptDecryptService.class })
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private User newUser;

    @Autowired
    private User persistedUser;

    @Autowired
    private User forUpdateUser;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserAssembler userAssembler;

    private OAuth2Authentication authentication;

    private static InMemoryTokenStore tokenStore = new InMemoryTokenStore();

    private OAuth2AccessToken token;

    private static final String USER_NAME = "testUser";

    private static final String USERS_URI = "/users";
    private static final String USERS_URL = "http://localhost" + USERS_URI;
    private final String FIELD_SORT_TITLE = "userName,asc";
    private final int PAGE_NUMBER = 1;
    private final String PAGE_NUMBER_STRING = "1";
    private final int PAGE_SIZE = 10;
    private final String PAGE_SIZE_STRING = "10";

    @MockBean
    private EncryptDecryptService encryptDecryptService;

    @Configuration
    @EnableResourceServer
    @EnableWebSecurity
    protected static class ResourceServerContext {
        @Bean
        public TokenStore tokenStore() {
            return tokenStore;
        }
    }

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(wac).apply(springSecurity()).build();
        tokenStore.clear();
        token = new DefaultOAuth2AccessToken("FOO");
        ClientDetails client = new BaseClientDetails("client", null, "read", "client_credentials", "ROLE_CLIENT");
        TestingAuthenticationToken userAuthentication = new TestingAuthenticationToken("testUser", "abc", "ROLE_ADMIN");
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("name", USER_NAME);
        userAuthentication.setDetails(userInfo);
        authentication = new OAuth2Authentication(
                new TokenRequest(null, "client", null, "client_credentials").createOAuth2Request(client),
                userAuthentication);
        tokenStore.storeAccessToken(token, authentication);
    }

    @Test
    public void testGetLoggedUser() throws Exception {
        ResultActions result = mockMvc.perform(get(USERS_URI + "/logged").header("Authorization", "Bearer FOO"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetLoggedUserNotExisting() throws Exception {
        given(userRepository.findByUserName(USER_NAME)).willReturn(new User());
        ResultActions result = mockMvc.perform(get(USERS_URI + "/logged").header("Authorization", "Bearer FOO"));
        result.andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        given(encryptDecryptService.encrypt(newUser.getUserPassword())).willReturn(USER_PASSWORD_ENCRYPTED);
        given(userRepository.save(any(User.class))).willReturn(persistedUser);
        given(userAssembler.toResource(persistedUser)).willCallRealMethod();
        ResultActions result = mockMvc.perform(post(USERS_URI).header("Authorization", "Bearer FOO")
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE)
                .content(Util.convertObjectToJsonString(newUser)));

        result.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._links.self.href").value(USERS_URL + "/" + persistedUser.getUserId()));
    }

    @Test
    public void testCreateUserBadRequest() throws Exception {
        User wrongUser = new User();
        ResultActions result = mockMvc.perform(post(USERS_URI).header("Authorization", "Bearer FOO")
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(wrongUser)));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUserError() throws Exception {
        given(encryptDecryptService.encrypt(newUser.getUserPassword())).willReturn(USER_PASSWORD_ENCRYPTED);
        given(userRepository.save(any(User.class))).willThrow(
                new ConstraintViolationException(null, new SQLException(), null));
        ResultActions result = mockMvc.perform(post(USERS_URI).header("Authorization", "Bearer FOO")
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(newUser)));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsersByCompany() throws Exception {
        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "userName");
        List<User> usersList = new ArrayList<User>();
        usersList.add(persistedUser);
        Page<User> users = new PageImpl<User>(usersList, pageRequest, 100);
        given(userRepository.findByCompanyId(COMPANY_ID, pageRequest)).willReturn(users);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", COMPANY_ID.toString());
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get(USERS_URI).params(mapOfParams).header("Authorization", "Bearer FOO")
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE)
                .content(Util.convertObjectToJsonString(users)));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetUsersWithoutCompanyId() throws Exception {
        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "userName");
        List<User> usersList = new ArrayList<User>();
        usersList.add(persistedUser);
        Page<User> pagedProducts = new PageImpl<User>(usersList, pageRequest, 100);
        given(userRepository.findByCompanyId(COMPANY_ID, pageRequest)).willReturn(pagedProducts);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get(USERS_URI).params(mapOfParams).header("Authorization", "Bearer FOO")
                .accept(MediaTypes.HAL_JSON_VALUE).contentType(MediaTypes.HAL_JSON_VALUE));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserBadRequest() throws Exception {
        given(userRepository.findOne(101L)).willReturn(null);
        mockMvc.perform(get(USERS_URI).header("Authorization", "Bearer FOO").param("userId", "101")).andExpect(
                status().isBadRequest());
    }

    @Test
    public void testGetUser() throws Exception {
        String encryptedPassword = "fb126394998693b116aed3d31fc12c401c41525e248a00203b201e2eec0074e4";
        newUser.setUserPassword(encryptedPassword);
        given(userRepository.findOne(USER_ID)).willReturn(persistedUser);
        given(userAssembler.toResource(persistedUser)).willCallRealMethod();
        ResultActions result = mockMvc.perform(get(USERS_URI + "/{userId}", USER_ID)
                .header("Authorization", "Bearer FOO").accept(MediaTypes.HAL_JSON_VALUE)
                .contentType(MediaTypes.HAL_JSON_VALUE).content(Util.convertObjectToJsonString(persistedUser)));
        result.andExpect(status().isOk()).andExpect(jsonPath("$._links.self.href").value(USERS_URL + "/" + USER_ID));
    }

    @Test
    public void testGetUserNotFound() throws Exception {
        given(userRepository.findOne(2L)).willReturn(null);
        mockMvc.perform(get(USERS_URI + "/{userId}", 2).header("Authorization", "Bearer FOO")).andExpect(
                status().isNotFound());
    }

    @Test
    public void testUpdateUser() throws Exception {
        given(userRepository.findOne(forUpdateUser.getUserId())).willReturn(forUpdateUser);
        given(encryptDecryptService.encrypt(forUpdateUser.getUserPassword())).willReturn(
                forUpdateUser.getUserPassword());
        given(userRepository.saveAndFlush(forUpdateUser)).willReturn(forUpdateUser);
        given(userAssembler.toResource(forUpdateUser)).willCallRealMethod();

        mockMvc.perform(
                put(USERS_URI).header("Authorization", "Bearer FOO").accept(HAL_JSON).contentType(HAL_JSON)
                        .content(convertObjectToJsonString(forUpdateUser))).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._links.self.href").value(USERS_URL + "/" + USER_ID));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        given(userRepository.findOne(persistedUser.getUserId())).willReturn(null);
        mockMvc.perform(
                put(USERS_URI).header("Authorization", "Bearer FOO").accept(HAL_JSON).contentType(HAL_JSON)
                        .content(convertObjectToJsonString(persistedUser))).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUserBadRequest1() throws Exception {
        User user = new User();
        mockMvc.perform(
                put(USERS_URI).header("Authorization", "Bearer FOO").accept(HAL_JSON).contentType(HAL_JSON)
                        .content(convertObjectToJsonString(user))).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateUserBadRequest2() throws Exception {
        given(userRepository.findOne(persistedUser.getUserId())).willThrow(
                new InvalidDataAccessApiUsageException("Missing id"));
        mockMvc.perform(
                put(USERS_URI).header("Authorization", "Bearer FOO").accept(HAL_JSON).contentType(HAL_JSON)
                        .content(convertObjectToJsonString(forUpdateUser))).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatUserUnsupportedMediaType() throws Exception {
        mockMvc.perform(
                put(USERS_URI).header("Authorization", "Bearer FOO").accept(HAL_JSON)
                        .content(convertObjectToJsonString(newUser))).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete(USERS_URI + "/{userId}", USER_ID).header("Authorization", "Bearer FOO")).andExpect(
                status().isNoContent());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        doThrow(IllegalArgumentException.class).when(userRepository).delete(USER_ID);

        mockMvc.perform(delete(USERS_URI + "/{userId}", USER_ID).header("Authorization", "Bearer FOO")).andExpect(
                status().isNotFound());
    }

}