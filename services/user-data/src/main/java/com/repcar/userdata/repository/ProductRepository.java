/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.userdata.domain.Product;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCompanyIdAndCreationDateGreaterThanOrderByCreationDateAsc(Long companyId, Timestamp timeStamp);

    List<Product> findAllByProductShopShopIdAndProductIdIn(Long shopId, List<Long> productIds);

    List<Product> findAllByCompanyIdAndProductIdIn(Long companyId, List<Long> productIds);
}
