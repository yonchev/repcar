/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.userdata.bean.RecommendationDetails;

@Repository
@Transactional
public interface RecommendationDetailsRepository extends JpaRepository<RecommendationDetails, Long>{

    RecommendationDetails findByCompanyId(Long companyId);

    RecommendationDetails findByApplicationName (String applicationName);
}
