/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.hibernate.validator.constraints.URL;

@Entity
public class RecommendationDetails {

    /* Marker interface for grouping validations to be applied at the time of updating a (existing) content. */
    public interface RecommendationDetailsUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of creating a (new) content. */
    public interface RecommendationDetailsCreate {
    }

    private Long recommendationDetailsId;
    private String applicationName;
    private String applicationAccessKey;
    private String productRecommenderUrl;
    private String categoryRecommenderUrl;
    private Long companyId;

    @Id
    @GeneratedValue
    @NotNull(message = "Please provide recommendationDetailsId!", groups = { RecommendationDetailsUpdate.class })
    @Null(message = "Please remove recommendationDetailsId!", groups = { RecommendationDetailsCreate.class })
    public Long getRecommendationDetailsId() {
        return recommendationDetailsId;
    }

    public void setRecommendationDetailsId(Long recommendationDetailsId) {
        this.recommendationDetailsId = recommendationDetailsId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationAccessKey() {
        return applicationAccessKey;
    }

    public void setApplicationAccessKey(String applicationAccessKey) {
        this.applicationAccessKey = applicationAccessKey;
    }

    @URL(groups = { RecommendationDetailsCreate.class, RecommendationDetailsUpdate.class })
    public String getProductRecommenderUrl() {
        return productRecommenderUrl;
    }

    public void setProductRecommenderUrl(String productRecommenderUrl) {
        this.productRecommenderUrl = productRecommenderUrl;
    }

    @URL(groups = { RecommendationDetailsCreate.class, RecommendationDetailsUpdate.class })
    public String getCategoryRecommenderUrl() {
        return categoryRecommenderUrl;
    }

    public void setCategoryRecommenderUrl(String categoryRecommenderUrl) {
        this.categoryRecommenderUrl = categoryRecommenderUrl;
    }

    @NotNull(groups = { RecommendationDetailsCreate.class, RecommendationDetailsUpdate.class })
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationAccessKey == null) ? 0 : applicationAccessKey.hashCode());
        result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
        result = prime * result + ((categoryRecommenderUrl == null) ? 0 : categoryRecommenderUrl.hashCode());
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((recommendationDetailsId == null) ? 0 : recommendationDetailsId.hashCode());
        result = prime * result + ((productRecommenderUrl == null) ? 0 : productRecommenderUrl.hashCode());
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
        RecommendationDetails other = (RecommendationDetails) obj;
        if (applicationAccessKey == null) {
            if (other.applicationAccessKey != null)
                return false;
        } else if (!applicationAccessKey.equals(other.applicationAccessKey))
            return false;
        if (applicationName == null) {
            if (other.applicationName != null)
                return false;
        } else if (!applicationName.equals(other.applicationName))
            return false;
        if (categoryRecommenderUrl == null) {
            if (other.categoryRecommenderUrl != null)
                return false;
        } else if (!categoryRecommenderUrl.equals(other.categoryRecommenderUrl))
            return false;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        if (recommendationDetailsId == null) {
            if (other.recommendationDetailsId != null)
                return false;
        } else if (!recommendationDetailsId.equals(other.recommendationDetailsId))
            return false;
        if (productRecommenderUrl == null) {
            if (other.productRecommenderUrl != null)
                return false;
        } else if (!productRecommenderUrl.equals(other.productRecommenderUrl))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("RecommendationDetails [recommendationDetailsId=")
                .append(recommendationDetailsId).append(", applicationName=").append(applicationName)
                .append(", applicationAccessKey=").append(applicationAccessKey).append(", productRecommenderUrl=")
                .append(productRecommenderUrl).append(", categoryRecommenderUrl=").append(categoryRecommenderUrl)
                .append(", companyId=").append(companyId).append("]").toString();
    }

}
