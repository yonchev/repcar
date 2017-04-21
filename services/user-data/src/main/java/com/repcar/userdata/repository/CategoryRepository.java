/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.userdata.domain.Category;

/**
 * @author <a href="mailto:rtodorova@repcarpro.com">Ralitsa Todorova</a>
 *
 */
@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCompanyIdAndCreationDateGreaterThanOrderByCreationDateAsc(Long companyId, Timestamp timeStamp);

    List<Category> findAllByCompanyIdAndCategoryIdIn(Long companyId, List<Long> categoryIds);

    List<Category> findAllByCategoryShopShopIdAndCategoryIdIn(Long shopId, List<Long> categoryIds);

}
