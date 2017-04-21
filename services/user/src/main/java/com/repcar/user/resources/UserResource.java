/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.resources;

import java.sql.Timestamp;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repcar.user.beans.User.Roles;

/**
 * @author <a href="mailto:mstancheva@repcarpro.com">Mihaela Stancheva</a>
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "users", value = "user")
public class UserResource extends ResourceSupport {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userImage;
    private Roles userRole;
    private Timestamp creationDate;
    private String userAttributes;

    @JsonCreator
    public UserResource() {

    }

    @JsonCreator
    public UserResource(@JsonProperty("userId") Long userId, @JsonProperty("userName") String userName,
            @JsonProperty("userEmail") String userEmail, @JsonProperty("userFirstName") String userFirstName,
            @JsonProperty("userLastName") String userLastName, @JsonProperty("userImage") String userImage,
            @JsonProperty("userRole") Roles roles, @JsonProperty("creationDate") Timestamp creationDate,
            @JsonProperty("userAttributes") String userAttributes) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userImage = userImage;
        this.userRole = roles;
        this.creationDate = creationDate;
        this.userAttributes = userAttributes;
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

    public String getUserImage() {
        return this.userImage;
    }

    public Roles getUserRole() {
        return this.userRole;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public String getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(String userAttributes) {
        this.userAttributes = userAttributes;
    }
}
