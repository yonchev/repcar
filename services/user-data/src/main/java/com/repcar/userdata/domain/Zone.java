/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * @author <a href="mailto:mstancheva@repcarpro.com">Mihaela Stancheva</a>
 */

@Entity
public class Zone {
    /* Marker interface for grouping validations to be applied at the time of updating a (existing) content. */
    public interface ZoneUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of creating a (new) content. */
    public interface ZoneCreate {
    }

    private Long zoneId;
    private String zoneName;
    private String locationMapHierarchy;
    private Long shopId;

    @Id
    @GeneratedValue
    @Column(nullable = false)
    @NotNull(message = "Please enter zoneId!", groups = { ZoneUpdate.class })
    @Null(message = "Please remove zoneId!", groups = { ZoneCreate.class })
    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    @NotNull(groups = { ZoneCreate.class, ZoneUpdate.class })
    @Pattern(regexp = "\\S.+",
            message = "The zoneName cannot be empty or start with whitespace.",
            groups = { ZoneCreate.class, ZoneUpdate.class })
    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    @NotNull(groups = { ZoneCreate.class, ZoneUpdate.class })
    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @NotNull(groups = { ZoneCreate.class, ZoneUpdate.class })
    @Pattern(regexp = "\\S.+",
            message = "The locationMapHierarchy cannot be empty or start with whitespace.",
            groups = { ZoneCreate.class, ZoneUpdate.class })
    public String getLocationMapHierarchy() {
        return locationMapHierarchy;
    }

    public void setLocationMapHierarchy(String locationMapHierarchy) {
        this.locationMapHierarchy = locationMapHierarchy;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((locationMapHierarchy == null) ? 0 : locationMapHierarchy.hashCode());
        result = prime * result + ((shopId == null) ? 0 : shopId.hashCode());
        result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
        result = prime * result + ((zoneName == null) ? 0 : zoneName.hashCode());
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
        Zone other = (Zone) obj;
        if (locationMapHierarchy == null) {
            if (other.locationMapHierarchy != null)
                return false;
        } else if (!locationMapHierarchy.equals(other.locationMapHierarchy))
            return false;
        if (shopId == null) {
            if (other.shopId != null)
                return false;
        } else if (!shopId.equals(other.shopId))
            return false;
        if (zoneId == null) {
            if (other.zoneId != null)
                return false;
        } else if (!zoneId.equals(other.zoneId))
            return false;
        if (zoneName == null) {
            if (other.zoneName != null)
                return false;
        } else if (!zoneName.equals(other.zoneName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Zone [zoneId=").append(zoneId).append(", zoneName=").append(zoneName)
                .append(", locationMapHierarchy=").append(locationMapHierarchy).append(", shopId=").append(shopId).append("]").toString();
    }
}
