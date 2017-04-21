/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.userdata.domain.RecommendationMetadata;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Repository
@Transactional
public interface RecommendationMetadataRepository extends JpaRepository<RecommendationMetadata, Long> {

}
