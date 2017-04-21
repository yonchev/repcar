/*
 * Copyright RepCar AD 2017
 */
package com.repcar.security.rest;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 *
 */

@RestController
public class UserController {

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
