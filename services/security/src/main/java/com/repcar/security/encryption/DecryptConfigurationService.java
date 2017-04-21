/*
 * Copyright RepCar AD 2017
 */
package com.repcar.security.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DecryptConfigurationService implements DecryptService {

    private static final Logger logger = LoggerFactory.getLogger(DecryptConfigurationService.class);

    @Autowired
    private RestTemplate configurationRestTemplate;

    @Value("${eureka.instance.securePortEnabled:false}")
    private boolean isSecure;

    @Override
    public String decrypt(String password) {
        if (password != null && !password.isEmpty()) {
            try {
                String decryptUrl = (isSecure ? "https://" : "http://") + "configuration/decrypt";
                password = configurationRestTemplate.postForObject(decryptUrl, password, String.class);
                logger.trace("Password decrypted successfully.");
            } catch (Exception e) {
                // TODO remove password for production
                logger.error("Cannot decrypt password {}", password);
                logger.debug(e.getMessage(), e);
            }
        }
        return password;
    }

}