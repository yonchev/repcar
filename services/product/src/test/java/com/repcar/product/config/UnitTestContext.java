/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.config;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.client.RestTemplate;

import com.repcar.product.beans.Category;
import com.repcar.product.beans.Company;
import com.repcar.product.beans.Product;
import com.repcar.product.beans.Shop;
import com.repcar.product.resources.ProductResource;

@Configuration
@EnableSpringDataWebSupport
@ComponentScan("com.repcar.product.controllers")
public class UnitTestContext {

    @Bean(name = "clientRestTemplate")
    public OAuth2RestOperations oAuth2RestOperations() {
        return mock(OAuth2RestOperations.class);
    }

    @Bean
    public Company createCompany() {
        Company company = new Company();
        company.setCompanyId(1L);
        return company;
    }

    @Bean
    public Shop createShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("abc");
        shop.setShopCountry("abc");
        shop.setShopCity("abc");
        shop.setShopAddress("abc");
        shop.setShopType("abc");
        shop.setShopUrl("abc");
        shop.setCompanyId(1L);
        return shop;
    }

    @Bean
    public Category createCategory(){
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("abc");
        category.setCompanyId(1L);
        return category;
    }

    @Bean(name = "restTemplate")
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        return template;
    }

    @Bean(name = "clientRestTemplate")
    public OAuth2RestOperations clientRestTemplate() {
        return mock(OAuth2RestOperations.class);
    }

    @Bean
    public Product createProduct() {
        Product product = new Product();
        product.setProductId(600L);
        product.setProductName("Topka");
        product.setProductPrice("2.6");
        product.setProductImage("car.jpg");
        product.setCompany(createCompany());
        List<Category> categories =  new ArrayList<>();
        categories.add(createCategory());
        product.setProductCategory(categories);
        product.setProductDescription("abc");
        product.setProductRfid("123465798");
        product.setProductNfc("123abc45697856984");
        List<Shop> shops = new ArrayList<>();
        shops.add(createShop());
        product.setProductShop(shops);
        return product;
    }

    @Bean
    public ProductResource createProductResource(){
        ProductResource productResource = new ProductResource();
        productResource.setProductId(600L);
        productResource.setProductName("Topka");
        productResource.setProductPrice("2.6");
        productResource.setProductImage("car.jpg");
        productResource.setCompanyId(1L);
        List<Category> categories =  new ArrayList<>();
        categories.add(createCategory());
        productResource.setProductCategory(categories);
        productResource.setProductDescription("abc");
        productResource.setProductRfid("123465798");
        productResource.setProductNfc("123abc45697856984");
        List<Shop> shops = new ArrayList<>();
        shops.add(createShop());
        productResource.setProductShop(shops);
        return productResource;
    }

}