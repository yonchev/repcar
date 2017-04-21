/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.resources;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Relation(collectionRelation = "recommendationDetails", value = "recommendationDetail")
public class RecommendationDetailsResource extends ResourceSupport {

    private Long recommendationDetailsId;
    private String applicationName;
    private String applicationAccessKey;
    private String productRecommenderUrl;
    private String categoryRecommenderUrl;
    private Long companyId;

    @JsonCreator
    public RecommendationDetailsResource(@JsonProperty("recommendationDetailsId") Long recommendationDetailsId,
            @JsonProperty("applicationName") String applicationName,
            @JsonProperty("applicationAccessKey") String applicationAccessKey,
            @JsonProperty("productRecommenderPort") String productRecommenderUrl,
            @JsonProperty("categoryRecommenderPort") String categoryRecommenderUrl,
            @JsonProperty("companyId") Long companyId) {
        this.recommendationDetailsId = recommendationDetailsId;
        this.applicationName = applicationName;
        this.applicationAccessKey = applicationAccessKey;
        this.productRecommenderUrl = productRecommenderUrl;
        this.categoryRecommenderUrl = categoryRecommenderUrl;
        this.companyId = companyId;
    }

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

    public String getProductRecommenderUrl() {
        return productRecommenderUrl;
    }

    public void setProductRecommenderUrl(String productRecommenderUrl) {
        this.productRecommenderUrl = productRecommenderUrl;
    }

    public String getCategoryRecommenderUrl() {
        return categoryRecommenderUrl;
    }

    public void setCategoryRecommenderPort(String categoryRecommenderUrl) {
        this.categoryRecommenderUrl = categoryRecommenderUrl;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((applicationAccessKey == null) ? 0 : applicationAccessKey.hashCode());
        result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
        result = prime * result + ((categoryRecommenderUrl == null) ? 0 : categoryRecommenderUrl.hashCode());
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((productRecommenderUrl == null) ? 0 : productRecommenderUrl.hashCode());
        result = prime * result + ((recommendationDetailsId == null) ? 0 : recommendationDetailsId.hashCode());
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
        RecommendationDetailsResource other = (RecommendationDetailsResource) obj;
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
        if (productRecommenderUrl == null) {
            if (other.productRecommenderUrl != null)
                return false;
        } else if (!productRecommenderUrl.equals(other.productRecommenderUrl))
            return false;
        if (recommendationDetailsId == null) {
            if (other.recommendationDetailsId != null)
                return false;
        } else if (!recommendationDetailsId.equals(other.recommendationDetailsId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("RecommendationDetailsResource [recommendationDetailsId=")
                .append(recommendationDetailsId).append(", applicationName=").append(applicationName)
                .append(", applicationAccessKey=").append(applicationAccessKey).append(", productRecommenderUrl=")
                .append(productRecommenderUrl).append(", categoryRecommenderUrl=").append(categoryRecommenderUrl)
                .append(", companyId=").append(companyId).append("]").toString();
    }

}
