/*
 * Copyright RepCar AD 2017
 */
package com.repcar.security.config;

import javax.sql.DataSource;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */
public class TransactionalTokenStore extends JdbcTokenStore {
    public TransactionalTokenStore(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public synchronized void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);
    }
}
