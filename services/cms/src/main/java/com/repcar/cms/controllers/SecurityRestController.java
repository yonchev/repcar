/*
 * Copyright RepCar AD 2017
 */
package com.repcar.cms.controllers;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class SecurityRestController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityRestController.class);

    @Autowired
    @Qualifier("clientRestTemplate")
    private OAuth2RestOperations restTemplate;

    @Value("${security.oauth2.client.clientId}") String clientId;
    @Value("${security.oauth2.client.clientSecret}") String clientSecret;

    @RequestMapping(value = "/token", method = POST)
    public ResponseEntity<String> create(@RequestParam(required = true) String username,
            @RequestParam(required = true) String password) throws UnsupportedEncodingException {

        HttpHeaders headers = new HttpHeaders();

        byte[] base64Token = (clientId + ":" + clientSecret).getBytes("UTF-8");
        byte[] encode;
        try {
            encode = Base64.encode(base64Token);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to encode basic authentication token");
            throw new BadCredentialsException("Failed to encode basic authentication token");
        }

        String encodedBasicToken = "Basic " + new String(encode);

        headers.set("Authorization", encodedBasicToken);
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);

        logger.debug("Try to post token request");

        ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                "http://security/oauth/token?grant_type=password&username=" + username + "&password=" + password,
                requestEntity, String.class);

        logger.debug("Token response " + tokenResponse.getBody());
        return new ResponseEntity<>(tokenResponse.getBody(), HttpStatus.OK);
    }

}