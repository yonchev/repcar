/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.domain;

import io.prediction.Event;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Entity
public class IdMap {
    @Id
    @GeneratedValue
    private Long mapId;
    private Long userId;
    private String ssid;
    private Timestamp creationDate;
    private String weakId;
    private boolean accepted;
    private Long companyId;
    private String email;

    @JsonCreator
    public IdMap(@JsonProperty("mapId") Long mapId, @JsonProperty("userId") Long userId,
            @JsonProperty("ssid") String ssid, @JsonProperty("creationDate") Timestamp creationDate,
            @JsonProperty("weakId") String weakId, @JsonProperty("accepted") boolean accepted,
            @JsonProperty("companyId") Long companyId, @JsonProperty("email") String email) {

        this.mapId = mapId;
        this.userId = userId;
        this.ssid = ssid;
        this.creationDate = creationDate;
        this.weakId = weakId;
        this.accepted = accepted;
        this.companyId = companyId;
        this.email = email;
    }

    @JsonCreator
    public IdMap(){

    }


    public Long getMapId() {
        return mapId;
    }

    @Generated(GenerationTime.INSERT)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSsid() {
        return ssid;
    }

    public String getWeakId() {
        return weakId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getEmail() {
        return email;
    }

    private Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("CREATION_DATE", creationDate);
        result.put("WEAK_ID", weakId);
        return result;
    }

    public Event toEvent() {
        return new Event().event("$set").entityType("user").entityId(weakId).properties(getProperties())
                .eventTime(DateTime.now());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (accepted ? 1231 : 1237);
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((mapId == null) ? 0 : mapId.hashCode());
        result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((weakId == null) ? 0 : weakId.hashCode());
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
        IdMap other = (IdMap) obj;
        if (accepted != other.accepted)
            return false;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (mapId == null) {
            if (other.mapId != null)
                return false;
        } else if (!mapId.equals(other.mapId))
            return false;
        if (ssid == null) {
            if (other.ssid != null)
                return false;
        } else if (!ssid.equals(other.ssid))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (weakId == null) {
            if (other.weakId != null)
                return false;
        } else if (!weakId.equals(other.weakId))
            return false;
        return true;
    }

}
