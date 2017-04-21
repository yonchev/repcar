/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Entity
public class Shop {

    /* Marker interface for grouping validations to be applied at the time of updating a (existing) content. */
    public interface ShopUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of creating a (new) content. */
    public interface ShopCreate {
    }

    @Id
    @GeneratedValue
    @NotNull(message = "Please enter shopId!", groups = { ShopUpdate.class })
    @Null(message = "Please remove shopId!", groups = { ShopCreate.class })
    @Column(nullable = false)
    private Long shopId;

    @Column(nullable = false)
    @NotNull(groups = { ShopUpdate.class, ShopCreate.class })
    @Pattern(regexp = "\\S.+", message = "The shop name cannot be empty or start with whitespace.", groups = {
            ShopUpdate.class, ShopCreate.class })
    private String shopName;

    @Column(nullable = false)
    @NotNull(groups = { ShopUpdate.class, ShopCreate.class })
    @Pattern(regexp = "\\S.+", message = "The shop country cannot be empty or start with whitespace.", groups = {
            ShopUpdate.class, ShopCreate.class })
    private String shopCountry;

    @Column(nullable = false)
    @NotNull(groups = { ShopUpdate.class, ShopCreate.class })
    @Pattern(regexp = "\\S.+", message = "The shop city cannot be empty or start with whitespace.", groups = {
            ShopUpdate.class, ShopCreate.class })
    private String shopCity;

    @Column(nullable = false)
    @NotNull(groups = { ShopUpdate.class, ShopCreate.class })
    @Pattern(regexp = "\\S.+", message = "The shop address cannot be empty or start with whitespace.", groups = {
            ShopUpdate.class, ShopCreate.class })
    private String shopAddress;

    @Column(nullable = false)
    private String shopUrl;

    @Column(nullable = false)
    private String shopType;

    @Column(nullable = false)
    @NotNull(groups = { ShopUpdate.class, ShopCreate.class })
    private Long companyId;

    public Shop() {
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCountry() {
        return this.shopCountry;
    }

    public void setShopCountry(String shopCountry) {
        this.shopCountry = shopCountry;
    }

    public String getShopCity() {
        return this.shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopAddress() {
        return this.shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopUrl() {
        return this.shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopType() {
        return this.shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public Long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((shopAddress == null) ? 0 : shopAddress.hashCode());
        result = prime * result + ((shopCity == null) ? 0 : shopCity.hashCode());
        result = prime * result + ((shopCountry == null) ? 0 : shopCountry.hashCode());
        result = prime * result + ((shopId == null) ? 0 : shopId.hashCode());
        result = prime * result + ((shopName == null) ? 0 : shopName.hashCode());
        result = prime * result + ((shopType == null) ? 0 : shopType.hashCode());
        result = prime * result + ((shopUrl == null) ? 0 : shopUrl.hashCode());
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
        Shop other = (Shop) obj;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (shopAddress == null) {
            if (other.shopAddress != null)
                return false;
        } else if (!shopAddress.equals(other.shopAddress))
            return false;
        if (shopCity == null) {
            if (other.shopCity != null)
                return false;
        } else if (!shopCity.equals(other.shopCity))
            return false;
        if (shopCountry == null) {
            if (other.shopCountry != null)
                return false;
        } else if (!shopCountry.equals(other.shopCountry))
            return false;
        if (shopId == null) {
            if (other.shopId != null)
                return false;
        } else if (!shopId.equals(other.shopId))
            return false;
        if (shopName == null) {
            if (other.shopName != null)
                return false;
        } else if (!shopName.equals(other.shopName))
            return false;
        if (shopType == null) {
            if (other.shopType != null)
                return false;
        } else if (!shopType.equals(other.shopType))
            return false;
        if (shopUrl == null) {
            if (other.shopUrl != null)
                return false;
        } else if (!shopUrl.equals(other.shopUrl))
            return false;
        return true;
    }

}
