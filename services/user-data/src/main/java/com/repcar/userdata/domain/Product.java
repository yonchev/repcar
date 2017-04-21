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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long productId;

    private Timestamp creationDate;

    private String productName;

    private String productPrice;

    private String productImage;

    private String productDescription;

    private String productRfid;

    private String productNfc;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> productCategory;

    @ManyToMany
    @JoinTable(name = "product_shop",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id"))
    private List<Shop> productShop = new ArrayList<Shop>();

    private Long companyId;

    @JsonCreator
    public Product() {
    }

    @JsonCreator
    public Product(@JsonProperty("productName") String productName, @JsonProperty("productPrice") String productPrice,
            @JsonProperty("productImage") String productImage,
            @JsonProperty("productDescription") String productDescription,
            @JsonProperty("productRfid") String productRfid, @JsonProperty("productNfc") String productNfc,
            @JsonProperty("productCategories") List<Category> productCategory,
            @JsonProperty("productShops") List<Shop> productShop, @JsonProperty("companyId") Long companyId,
            @JsonProperty("creationDate") Timestamp creationDate) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productRfid = productRfid;
        this.productNfc = productNfc;
        this.productCategory = productCategory;
        this.productShop = productShop;
        this.companyId = companyId;
        this.creationDate = creationDate;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    @Generated(GenerationTime.INSERT)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRfid() {
        return this.productRfid;
    }

    public void setProductRfid(String productRfid) {
        this.productRfid = productRfid;
    }

    public String getProductNfc() {
        return this.productNfc;
    }

    public void setProductNfc(String productNFC) {
        this.productNfc = productNFC;
    }

    public List<Shop> getProductShop() {
        return productShop;
    }

    public void setProductShop(List<Shop> productShop) {
        this.productShop = productShop;
    }

    public List<Category> getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(List<Category> productCategory) {
        this.productCategory = productCategory;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    private Map<String, Object> getProperties() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("creation_date", creationDate.toInstant().toString());
        if (productName != null) {
            result.put("product_name", ImmutableList.of(productName));
        }
        if (productCategory != null && !productCategory.isEmpty()) {
            ArrayList<String> categories = new ArrayList<String>();
            for (Category cat : productCategory) {
                categories.add("c" + cat.getCategoryId());
            }
            result.put("category", categories);
        }
        result.put("company_id", ImmutableList.of(companyId));
        return result;
    }

    public Event toEvent() {
        return new Event().event("$set").entityType("item").entityId("i" + productId).properties(getProperties())
                .eventTime(DateTime.now());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((productCategory == null) ? 0 : productCategory.hashCode());
        result = prime * result + ((productShop == null) ? 0 : productShop.hashCode());
        result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
        result = prime * result + ((productRfid == null) ? 0 : productRfid.hashCode());
        result = prime * result + ((productNfc == null) ? 0 : productNfc.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((productImage == null) ? 0 : productImage.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((productPrice == null) ? 0 : productPrice.hashCode());
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
        Product other = (Product) obj;
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
        if (productCategory == null) {
            if (other.productCategory != null)
                return false;
        } else if (!productCategory.equals(other.productCategory))
            return false;
        if (productShop == null) {
            if (other.productShop != null)
                return false;
        } else if (!productShop.equals(other.productShop))
            return false;
        if (productDescription == null) {
            if (other.productDescription != null)
                return false;
        } else if (!productDescription.equals(other.productDescription))
            return false;
        if (productRfid == null) {
            if (other.productRfid != null)
                return false;
        } else if (!productRfid.equals(other.productRfid))
            return false;
        if (productNfc == null) {
            if (other.productNfc != null)
                return false;
        } else if (!productNfc.equals(other.productNfc))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (productImage == null) {
            if (other.productImage != null)
                return false;
        } else if (!productImage.equals(other.productImage))
            return false;
        if (productName == null) {
            if (other.productName != null)
                return false;
        } else if (!productName.equals(other.productName))
            return false;
        if (productPrice == null) {
            if (other.productPrice != null)
                return false;
        } else if (!productPrice.equals(other.productPrice))
            return false;
        return true;
    }

}
