/*
 * Copyright RepCar AD 2017
 */
package com.repcar.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.repcar.security.encryption.DecryptService;
import com.repcar.security.service.UserDAO;

/**
 * @author <a href="mailto:tslavkov@repcarpro.com">Tihomir Slavkov</a>
 */

@Configuration
@EnableGlobalAuthentication
public class SecurityConfig extends GlobalAuthenticationConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DecryptService decryptService;

    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        logger.info("Configured userDetailsService.");
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                try {
                    com.repcar.security.model.User user = userDAO.getUserByUserName(username);
                    if (!user.getUserName().equals(username)) {
                        throw new UsernameNotFoundException("DBUser does not exist");
                    }

                    return new User(user.getUserName(), decryptService.decrypt(user.getUserPassword()), true, true,
                            true, true, AuthorityUtils.createAuthorityList(user.getUserRole()));
                } catch (Exception e) {
                    throw new UsernameNotFoundException("DBUser does not exist");
                }
            }
        };
    }

    @Bean
    ResourceServerConfigurer resourceServerConfigurer() {
        return new ResourceServerConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http.authorizeRequests()
                        // allow anonymous access to these endpoints
                        .antMatchers("/info", "/env", "/health", "/configprops", "/metrics").permitAll()
                        // everything else requires authentication and in the case of admin console user - authorization
                        .antMatchers("/oauth/token*").hasAnyRole("CLIENT")
                        .anyRequest().authenticated();
            }
        };
    }
}
