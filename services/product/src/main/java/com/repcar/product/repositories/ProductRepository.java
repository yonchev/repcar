/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.product.beans.Product;

@Repository
@Transactional
public interface ProductRepository extends CustomProductRepository<Product, Long> {

    Page<Product> findAllByProductShopShopId(Long shopId, Pageable pageable);

    Page<Product> findAllByCompanyCompanyId(Long companyId, Pageable pageable);

    List<Product> findAllByProductShopShopIdAndProductIdIn(Long shopId, List<Long> productId);

    List<Product> findAllByCompanyCompanyIdAndProductIdIn(Long companyId, List<Long> productId);

    List<Product> findAllByProductIdIn(List<Long> productId);

    Page<Product> findAllByCreationDateGreaterThanOrderByCreationDateAsc(Timestamp timeStamp, Pageable pageable);

    Product findByProductRfid(String productRfid);

    Product findByProductNfc(String productNfc);

}
