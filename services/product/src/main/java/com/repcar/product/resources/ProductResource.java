/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.resources;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.repcar.product.beans.Category;
import com.repcar.product.beans.Shop;

@Relation(collectionRelation = "products", value = "product")
public class ProductResource extends ResourceSupport {
    private Long productId;
    private String productName;
    private String productPrice;
    private String productImage;
    private String productDescription;
    private String productAttributes;
    private String productRfid;
    private String productNfc;
    private Long companyId;
    private List<Category> productCategory;
    private List<Shop> productShop;
    private Timestamp creationDate;

    @JsonCreator
    public ProductResource(){
    }

    @JsonCreator
    public ProductResource(@JsonProperty("productId") Long productId, @JsonProperty("productName") String productName,
            @JsonProperty("productPrice") String productPrice, @JsonProperty("productImage") String productImage,
            @JsonProperty("productDescription") String productDescription,
            @JsonProperty("productAttributes") String productAttributes,
            @JsonProperty("productRfid") String productRfid, @JsonProperty("productNfc") String productNfc,
            @JsonProperty("productCategories") List<Category> productCategory,
            @JsonProperty("productShops") List<Shop> productShop, @JsonProperty("companyId") Long companyId,
            @JsonProperty("creationDate") Timestamp creationDate) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productAttributes = productAttributes;
        this.productRfid = productRfid;
        this.productNfc = productNfc;
        this.productCategory = productCategory;
        this.productShop = productShop;
        this.companyId = companyId;
        this.creationDate = creationDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
        return productRfid;
    }

    public void setProductRfid(String productRfid) {
        this.productRfid = productRfid;
    }

    public String getProductNfc() {
        return productNfc;
    }

    public void setProductNfc(String productNfc) {
        this.productNfc = productNfc;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public List<Category> getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(List<Category> productCategory) {
        this.productCategory = productCategory;
    }

    public List<Shop> getProductShop() {
        return productShop;
    }

    public void setProductShop(List<Shop> productShop) {
        this.productShop = productShop;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public String getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(String productAttributes) {
        this.productAttributes = productAttributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((productCategory == null) ? 0 : productCategory.hashCode());
        result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
        result = prime * result + ((productAttributes == null) ? 0 : productAttributes.hashCode());
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
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductResource other = (ProductResource) obj;
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
