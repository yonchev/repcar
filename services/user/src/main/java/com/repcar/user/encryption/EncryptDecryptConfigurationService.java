/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EncryptDecryptConfigurationService implements EncryptDecryptService {

    private static final Logger logger = LoggerFactory.getLogger(EncryptDecryptConfigurationService.class);

    @Autowired
    private RestTemplate configurationRestTemplate;

    @Value("${eureka.instance.securePortEnabled:false}")
    private boolean isSecure;

    @Override
    public String encrypt(String password) {
        if (password != null && !password.isEmpty()) {
            try {
                String encryptUrl = (isSecure ? "https://" : "http://") + "configuration/encrypt";
                password = configurationRestTemplate.postForObject(encryptUrl, password, String.class);
                logger.trace("Password encrypted successfully.");
            } catch (Exception e) {
                logger.error("Cannot encrypt password.");
                logger.debug(e.getMessage(), e);
            }
        }
        return password;
    }
}
