/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Entity
public class RecommendationMetadata {
    @Id
    @GeneratedValue
    private Long id;

    private Timestamp lastImportedProductTime;
    private Timestamp lastImportedCategoryTime;
    private Long companyId;

    public RecommendationMetadata(){
    }

    public RecommendationMetadata(Timestamp lastImportedProductTime, Timestamp lastImportedCategoryTime, Long companyId){
        this.lastImportedCategoryTime = lastImportedCategoryTime;
        this.lastImportedProductTime = lastImportedProductTime;
        this.companyId = companyId;
    }

    /**
     * @return the lastImportedProductTime
     */
    public Timestamp getLastImportedProductTime() {
        return lastImportedProductTime;
    }

    /**
     * @param lastImportedProductTime
     *            the lastImportedProductTime to set
     */
    public void setLastImportedProductTime(Timestamp lastImportedProductTime) {
        this.lastImportedProductTime = lastImportedProductTime;
    }

    /**
     * @return the lastImportedCategoryTime
     */
    public Timestamp getLastImportedCategoryTime() {
        return lastImportedCategoryTime;
    }

    /**
     * @param lastImportedCategoryTime
     *            the lastImportedCategoryTime to set
     */
    public void setLastImportedCategoryTime(Timestamp lastImportedCategoryTime) {
        this.lastImportedCategoryTime = lastImportedCategoryTime;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RecommendationMetadata [id=").append(id).append(", lastImportedProductTime=")
                .append(lastImportedProductTime).append(", lastImportedCategoryTime=").append(lastImportedCategoryTime)
                .append(", companyId=").append(companyId).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastImportedCategoryTime == null) ? 0 : lastImportedCategoryTime.hashCode());
        result = prime * result + ((lastImportedProductTime == null) ? 0 : lastImportedProductTime.hashCode());
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
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
        RecommendationMetadata other = (RecommendationMetadata) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lastImportedCategoryTime == null) {
            if (other.lastImportedCategoryTime != null)
                return false;
        } else if (!lastImportedCategoryTime.equals(other.lastImportedCategoryTime))
            return false;
        if (lastImportedProductTime == null) {
            if (other.lastImportedProductTime != null)
                return false;
        } else if (!lastImportedProductTime.equals(other.lastImportedProductTime))
            return false;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        return true;
    }

}
