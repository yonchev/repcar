/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import java.util.List;

@Configuration
public class OauthClientConfig {


    @Autowired
    private ClientHttpRequestFactory requestFactory;

    @Bean(name = "clientRestTemplate")
    @LoadBalanced
    public OAuth2RestOperations clientRestTemplate(@Value("${security.oauth2.client.accessTokenUri}") String tokenUrl,
            @Value("${security.oauth2.client.clientId}") String clientId,
            @Value("${security.oauth2.client.clientSecret}") String clientSecret,
            @Value("${security.oauth2.client.scope}") List<String> scopes) {

        OAuth2RestTemplate template = new OAuth2RestTemplate(clientResourceDetails(tokenUrl, clientId, clientSecret,
                scopes), new DefaultOAuth2ClientContext());
        return prepareClientTemplate(template);
    }

    private OAuth2RestTemplate prepareClientTemplate(OAuth2RestTemplate template) {
        template.setAccessTokenProvider(clientAccessTokenProvider());
        template.setRequestFactory(requestFactory);
        return template;
    }

    private AccessTokenProvider clientAccessTokenProvider() {
        ClientCredentialsAccessTokenProvider accessTokenProvider = new ClientCredentialsAccessTokenProvider();
        accessTokenProvider.setRequestFactory(requestFactory);
        return accessTokenProvider;
    }

    private OAuth2ProtectedResourceDetails clientResourceDetails(String tokenUrl, String clientId, String clientSecret,
            List<String> scopes) {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setScope(scopes);
        return resource;
    }
}