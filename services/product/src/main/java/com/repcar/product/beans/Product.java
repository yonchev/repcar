/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.validation.annotation.Validated;

import com.repcar.product.configs.ProductValidator;

@Entity
@ProductValidator(groups = { Product.ProductCreate.class, Product.ProductUpdate.class })
@Validated({ Product.ProductCreate.class, Product.ProductUpdate.class })
@Table(name = "product")
public class Product {

    /* Marker interface for grouping validations to be applied at the time of updating a (existing) category. */
    public interface ProductUpdate {
    }

    /* Marker interface for grouping validations to be applied at the time of creating a (new) category. */
    public interface ProductCreate {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    @NotNull(message = "Please enter productId!", groups = { ProductUpdate.class })
    @Null(message = "Please remove productId!", groups = { ProductCreate.class })
    private Long productId;

    @NotNull(groups = { Product.ProductCreate.class, Product.ProductUpdate.class })
    @Pattern(regexp = "\\S.+", message = "The productName cannot be empty or start with whitespace.")
    @Size(min = 1, max = 45)
    private String productName;

    @NotNull(groups = { Product.ProductCreate.class, Product.ProductUpdate.class })
    @Pattern(regexp = "\\S.+", message = "The productPrice cannot be empty or start with whitespace.")
    @Size(min = 1, max = 45)
    private String productPrice;

    private String productImage;

    private String productDescription;

    private String productAttributes;

    private String productRfid;

    private String productNfc;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Timestamp creationDate;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> productCategory = new ArrayList<Category>();

    @ManyToMany
    @JoinTable(name = "product_shop",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id"))
    private List<Shop> productShop = new ArrayList<Shop>();

    public Product() {
    }


    public String getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(String productAttributes) {
        this.productAttributes = productAttributes;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return this.productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return this.productDescription;
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

    public List<Category> getProductCategory() {
        return this.productCategory;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Generated(GenerationTime.INSERT)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((company == null) ? 0 : company.hashCode());
        result = prime * result + ((productCategory == null) ? 0 : productCategory.hashCode());
        result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
        result = prime * result + ((productRfid == null) ? 0 : productRfid.hashCode());
        result = prime * result + ((productNfc == null) ? 0 : productNfc.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((productImage == null) ? 0 : productImage.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + ((productPrice == null) ? 0 : productPrice.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + ((productAttributes == null) ? 0 : productAttributes.hashCode());
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
        if (company == null) {
            if (other.company != null)
                return false;
        } else if (!company.equals(other.company))
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
        if (productAttributes == null) {
            if (other.productAttributes != null)
                return false;
        } else if (!productAttributes.equals(other.productAttributes))
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
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Product [productId=").append(productId).append(", productName=")
                .append(productName).append(", productPrice=").append(productPrice).append(", productImage=")
                .append(productImage).append(", productDescription=").append(productDescription)
                .append(", productAttributes=").append(productAttributes)
                .append(", productCategory=").append(productCategory).append(", productShop=").append(productShop)
                .append(", company=").append(company).append(", creationDate=").append(creationDate)
                .append(", productRFID=").append(productRfid).append(", productNFC=").append(productNfc).append("]")
                .toString();
    }

}
