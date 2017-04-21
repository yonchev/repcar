/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.beans;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Relation(collectionRelation = "idmaps", value = "idmap")
public class IdMapResource extends ResourceSupport {

    private Long mapId;
    private String ssid;
    private String weakId;
    private String macAddress;
    private UserResource user;
    private boolean accepted;
    private Long companyId;
    private String email;

    @JsonCreator
    public IdMapResource() {

    }

    @JsonCreator
    public IdMapResource(@JsonProperty("ssid") String ssid, @JsonProperty("weakId") String weakId,
            @JsonProperty("user") UserResource user, @JsonProperty("macAddress") String macAddress,
            @JsonProperty("accepted") boolean accepted, @JsonProperty("companyId") Long companyId,
            @JsonProperty("email") String email) {
        this.ssid = ssid;
        this.weakId = weakId;
        this.setUser(user);
        this.setMacAddress(macAddress);
        this.accepted = accepted;
        this.companyId = companyId;
        this.email = email;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getWeakId() {
        return weakId;
    }

    public void setWeakId(String weakId) {
        this.weakId = weakId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public UserResource getUser() {
        return user;
    }

    public void setUser(UserResource user) {
        this.user = user;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
