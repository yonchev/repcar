/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.repository.RecommendationDetailsRepository;
import com.repcar.userdata.rest.RecommendationDetailsController;

import io.prediction.EngineClient;
import io.prediction.EventClient;

@Service
public class ConnectionsService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionsService.class);

    @Value("${predictionio.eventserver.url}")
    private String eventServerUrl;

    @Autowired
    private RecommendationDetailsRepository recommendationDetailsRepository;

    private Map<Long, EngineClient> productEngines = new HashMap<>();

    private Map<Long, EngineClient> categoryEngines = new HashMap<>();

    private Map<Long, EventClient> eventClients = new HashMap<>();

    public Map<Long, EngineClient> getProductEngines() {
        return this.productEngines;
    }

    public Map<Long, EngineClient> getCategoryEngines() {
        return this.categoryEngines;
    }

    public Map<Long, EventClient> getEventClients() {
        return this.eventClients;
    }

    public void addEventClient(Long companyId) {
        if (!getEventClients().containsKey(companyId)) {
            RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);
            if (details != null && details.getApplicationAccessKey() != null) {
                logger.info("Found recommendationDetails {} for companyId {}.", details, companyId);
                logger.info("Adding new event client for company {} to existing connections.", companyId);
                getEventClients().putIfAbsent(companyId,
                        new EventClient(details.getApplicationAccessKey(), eventServerUrl));
            } else {
                logger.error("No application access key found for companyId {}.", companyId);
                throw new IllegalArgumentException("No application access key found for companyId " + companyId);
            }
        }
    }

    public synchronized void addProductEngine(Long companyId) {
        if (!getProductEngines().containsKey(companyId)) {
            RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);
            if (details != null && details.getProductRecommenderUrl() != null) {
                logger.info("Found recommendationDetails {} for companyId {}.", details, companyId);
                logger.info("Adding new productRecommendationEngine for company {} to existiong connections.", companyId);
                getProductEngines().putIfAbsent(companyId, new EngineClient(details.getProductRecommenderUrl()));
            } else {
                logger.error("No product recommender URL found for companyId {}.", companyId);
            }
        }
    }

    public synchronized void addCategoryEngine(Long companyId) {
        if (!getCategoryEngines().containsKey(companyId)) {
            RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);
            if (details != null && details.getCategoryRecommenderUrl() != null) {
                logger.info("Found recommendationDetails {} for companyId {}.", details, companyId);
                logger.info("Adding new categoryRecommendationEngine for company {} to existiong connections.", companyId);
                getCategoryEngines().putIfAbsent(companyId, new EngineClient(details.getCategoryRecommenderUrl()));
            } else {
                logger.error("No category recommender URL found for companyId {}.", companyId);
            }
        }

    }
}
