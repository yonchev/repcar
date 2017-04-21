/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.domain;

import io.prediction.Event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.DateTime;

import com.google.common.collect.ImmutableList;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long categoryId;

    private String categoryName;

    private String categoryDescription;

    private Long companyId;

    @ManyToMany
    @JoinTable(name = "category_zone",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "zone_id"))
    private List<Zone> categoryZone = new ArrayList<Zone>();

    @ManyToMany
    @JoinTable(name = "category_shop",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id"))
    private List<Shop> categoryShop = new ArrayList<Shop>();

    private Timestamp creationDate;

    public Category() {
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public List<Zone> getCategoryZone() {
        return categoryZone;
    }

    public void setCategoryZone(List<Zone> categoryZone) {
        this.categoryZone = categoryZone;
    }

    public List<Shop> getCategoryShop() {
        return categoryShop;
    }

    public void setCategoryShop(List<Shop> categoryShop) {
        this.categoryShop = categoryShop;
    }

    @Generated(GenerationTime.INSERT)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    private Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("creation_date", creationDate.toInstant().toString());
        if (categoryName != null) {
            result.put("category_name", ImmutableList.of(categoryName));
        }
        return result;
    }

    public Event toEvent() {
        return new Event().event("$set").entityType("item").entityId("c" + categoryId).properties(getProperties())
                .eventTime(DateTime.now());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
        result = prime * result + ((categoryName == null) ? 0 : categoryName.hashCode());
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((categoryDescription == null) ? 0 : categoryDescription.hashCode());
        result = prime * result + ((categoryShop == null) ? 0 : categoryShop.hashCode());
        result = prime * result + ((categoryZone == null) ? 0 : categoryZone.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
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
        Category other = (Category) obj;
        if (categoryId == null) {
            if (other.categoryId != null)
                return false;
        } else if (!categoryId.equals(other.categoryId))
            return false;
        if (categoryName == null) {
            if (other.categoryName != null)
                return false;
        } else if (!categoryName.equals(other.categoryName))
            return false;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (categoryZone == null) {
            if (other.categoryZone != null)
                return false;
        } else if (!categoryZone.equals(other.categoryZone))
            return false;
        if (categoryShop == null) {
            if (other.categoryShop != null)
                return false;
        } else if (!categoryShop.equals(other.categoryShop))
            return false;
        if (categoryDescription == null) {
            if (other.categoryDescription != null)
                return false;
        } else if (!categoryDescription.equals(other.categoryDescription))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        return true;
    }

}
