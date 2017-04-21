/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repcar.notification.configuration.NotificationValidator;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */

@Entity
@NotificationValidator(groups = { Notification.NotificationCreate.class, Notification.NotificationUpdate.class })
@Validated({ Notification.NotificationCreate.class, Notification.NotificationUpdate.class })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    private Long notificationId;
    private String notificationUrl;
    private Events eventType;
    private Long companyId;
    private Long shopId;

    public enum Events {
        INOUT, MOVEMENT
    }

    /* Marker interface for grouping validations to be applied at the time of updating a (existing) content. */
    public interface NotificationUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of creating a (new) content. */
    public interface NotificationCreate {
    }

    @JsonCreator
    public Notification() {
    }

    @JsonCreator
    public Notification(@JsonProperty("notificationUrl") String notificationUrl,
            @JsonProperty("eventType") Events eventType, @JsonProperty("companyId") Long companyId,
            @JsonProperty("shopId") Long shopId) {
        this.notificationUrl = notificationUrl;
        this.eventType = eventType;
        this.companyId = companyId;
        this.shopId = shopId;
    }

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    @NotNull(groups = { NotificationUpdate.class }, message = "Please enter notificationId!")
    @Null(groups = { NotificationCreate.class }, message = "Please remove notificationId!")
    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationUrlId) {
        this.notificationId = notificationUrlId;
    }

    @NotNull(message = "The eventType must not be null!",
            groups = { NotificationCreate.class, NotificationUpdate.class })
    @Enumerated(EnumType.STRING)
    public Events getEventType() {
        return eventType;
    }

    public void setEventType(Events eventType) {
        this.eventType = eventType;
    }

    @NotNull(message = "The notificationUrl must not be null!",
            groups = { NotificationCreate.class, NotificationUpdate.class })
    @Pattern(regexp = "\\S.+", message = "The notificationUrl cannot be empty or start with whitespace.")
    @URL(groups = { NotificationCreate.class, NotificationUpdate.class })
    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    @NotNull(groups = { NotificationCreate.class, NotificationUpdate.class }, message = "Please enter companyId!")
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
        result = prime * result + ((notificationUrl == null) ? 0 : notificationUrl.hashCode());
        result = prime * result + ((shopId == null) ? 0 : shopId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Notification other = (Notification) obj;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (eventType != other.eventType)
            return false;
        if (notificationId == null) {
            if (other.notificationId != null)
                return false;
        } else if (!notificationId.equals(other.notificationId))
            return false;
        if (notificationUrl == null) {
            if (other.notificationUrl != null)
                return false;
        } else if (!notificationUrl.equals(other.notificationUrl))
            return false;
        if (shopId == null) {
            if (other.shopId != null)
                return false;
        } else if (!shopId.equals(other.shopId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Notification [notificationId=").append(notificationId)
                .append(", notificationUrl=").append(notificationUrl).append(", notificationType=").append(eventType)
                .append(", companyId=").append(companyId).append(", shopId=").append(shopId).append("]").toString();
    }

}
