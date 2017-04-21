package com.repcar.product.configs;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.repcar.product.beans.Category;
import com.repcar.product.beans.Product;
import com.repcar.product.beans.Shop;
import com.repcar.product.repositories.ShopRepository;

public class ProductConstraintValidator implements ConstraintValidator<ProductValidator, Product> {

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public void initialize(ProductValidator arg0) {
    }

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext arg1) {

        List<Long> shopIds = new ArrayList<>();
        for(Category category:product.getProductCategory()){
            if(!(category.getCategoryId() instanceof Long)){
                return false;
            }
        }
        for (Shop shop : product.getProductShop()) {
            if(!(shop.getShopId() instanceof Long)){
                return false;
            }
            shopIds.add(shop.getShopId());
        }
        if (!shopIds.isEmpty()) {
            List<Shop> shops = shopRepository.findAllByShopIdIn(shopIds);
            if (shops.isEmpty()) {
                return false;
            }
            for (Shop shop : shops) {
                if (!Optional.fromNullable(shop.getCompanyId()).equals(
                        Optional.fromNullable(product.getCompany().getCompanyId()))) {
                    return false;
                }
            }
        }
        return true;
    }

}
