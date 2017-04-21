/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.repcar.product.beans.Product;
import com.repcar.product.controllers.ProductController;
import com.repcar.product.resources.ProductResource;

@Service
public class ProductAssembler extends ResourceAssemblerSupport<Product, ProductResource> {

    public ProductAssembler() {
        super(ProductController.class, ProductResource.class);
    }

    public ProductResource toResource(Product product) {
        ProductResource resource = new ProductResource(product.getProductId(), product.getProductName(),
                product.getProductPrice(), product.getProductImage(), product.getProductDescription(),
                product.getProductAttributes(),
                product.getProductRfid(), product.getProductNfc(), product.getProductCategory(),
                product.getProductShop(), product.getCompany().getCompanyId(), product.getCreationDate());
        resource.add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
        return resource;
    }
}
