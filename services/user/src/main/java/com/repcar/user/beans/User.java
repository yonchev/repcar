/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class User extends ResourceSupport {

    public interface Update {
    }

    public interface Create {
    }

    private Long userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userImage;
    private String userAttributes;
    private Roles userRole;
    private Long companyId;
    private Timestamp creationDate;

    @JsonCreator
    public User() {
    }

    @JsonCreator
    public User(@JsonProperty("userName") String userName, @JsonProperty("userEmail") String userEmail,
            @JsonProperty("userPassword") String userPassword, @JsonProperty("userFirstName") String userFirstName,
            @JsonProperty("userLastName") String userLastName, @JsonProperty("userImage") String userImage,
            @JsonProperty("userRole") Roles userRole, @JsonProperty("companyId") Long companyId) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userImage = userImage;
        this.userRole = userRole;
        this.companyId = companyId;
    }

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    @Null(groups = { Create.class })
    @NotNull(groups = { Update.class })
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Size(min = 1, max = 80)
    @Column(length = 80)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Size(min = 1, max = 80)
    @Column(length = 80)
    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Size(min = 1, max = 80)
    @Column(length = 80)
    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Size(min = 1, max = 45)
    @Column(length = 45)
    public String getUserFirstName() {
        return this.userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Size(min = 1, max = 45)
    @Column(length = 45)
    public String getUserLastName() {
        return this.userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    @Column(length = 45)
    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @NotNull(groups = { Create.class, Update.class })
    @Enumerated(EnumType.STRING)
    public Roles getUserRole() {
        return this.userRole;
    }

    public void setUserRole(Roles userRole) {
        this.userRole = userRole;
    }

    @NotNull(groups = { Create.class, Update.class })
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public enum Roles {
        ROLE_ADMIN, ROLE_USER, ROLE_ANONYMOUS, ROLE_OPERATOR
    }

    public String getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(String userAttributes) {
        this.userAttributes = userAttributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((userEmail == null) ? 0 : userEmail.hashCode());
        result = prime * result + ((userFirstName == null) ? 0 : userFirstName.hashCode());
        result = prime * result + ((userImage == null) ? 0 : userImage.hashCode());
        result = prime * result + ((userLastName == null) ? 0 : userLastName.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        result = prime * result + ((userRole == null) ? 0 : userRole.hashCode());
        result = prime * result + ((userAttributes == null) ? 0 : userAttributes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (userEmail == null) {
            if (other.userEmail != null)
                return false;
        } else if (!userEmail.equals(other.userEmail))
            return false;
        if (userFirstName == null) {
            if (other.userFirstName != null)
                return false;
        } else if (!userFirstName.equals(other.userFirstName))
            return false;
        if (userImage == null) {
            if (other.userImage != null)
                return false;
        } else if (!userImage.equals(other.userImage))
            return false;
        if (userLastName == null) {
            if (other.userLastName != null)
                return false;
        } else if (!userLastName.equals(other.userLastName))
            return false;
        if (userAttributes == null) {
            if (other.userAttributes != null)
                return false;
        } else if (!userAttributes.equals(other.userAttributes))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        if (userRole != other.userRole)
            return false;
        return true;
    }

}
