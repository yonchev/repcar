/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.beans;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResource extends ResourceSupport {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userRole;
    private String userImage;

    @JsonCreator
    public UserResource(@JsonProperty("userId") Long userId, @JsonProperty("userName") String userName,
            @JsonProperty("userEmail") String userEmail, @JsonProperty("userFirstName") String userFirstName,
            @JsonProperty("userLastName") String userLastName, @JsonProperty("userRole") String userRole,
            @JsonProperty("userImage") String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userRole = userRole;
        this.userImage = userImage;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserFirstName() {
        return this.userFirstName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserImage() {
        return userImage;
    }

}
