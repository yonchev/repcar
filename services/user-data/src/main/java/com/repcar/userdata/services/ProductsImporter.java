/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.repcar.userdata.domain.Category;
import com.repcar.userdata.domain.Product;
import com.repcar.userdata.domain.RecommendationMetadata;
import com.repcar.userdata.repository.CategoryRepository;
import com.repcar.userdata.repository.ProductRepository;
import com.repcar.userdata.repository.RecommendationMetadataRepository;

import io.prediction.Event;

/**
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@Service
public class ProductsImporter {

    private static final Logger logger = LoggerFactory.getLogger(ProductsImporter.class);

    @Autowired
    private RecommendationMetadataRepository recommendationMetadataRepository;

    // TODO replace this with OAuth2RestOperation once when we are able to run it outside of a web thread
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EventServerConnector eventServerConnector;

    @Scheduled(cron = "${import.cron}")
    public void importData() {
        List<RecommendationMetadata> lastTime = recommendationMetadataRepository.findAll();
        for(RecommendationMetadata meta: lastTime){
            List<Product> products = productRepository.findByCompanyIdAndCreationDateGreaterThanOrderByCreationDateAsc(
                    meta.getCompanyId(), meta.getLastImportedProductTime());
            List<Category> categories = categoryRepository.findByCompanyIdAndCreationDateGreaterThanOrderByCreationDateAsc(
                    meta.getCompanyId(), meta.getLastImportedCategoryTime());
        if (!(products.isEmpty() && categories.isEmpty())) {
                logger.debug("Adding new events for meta {}", meta);
                if (!products.isEmpty()) {
                    if (importImpl(meta.getCompanyId(),
                            products.stream().map(Product::toEvent).collect(Collectors.toList()), meta)) {
                        meta.setLastImportedProductTime(products.get(products.size() - 1).getCreationDate());
                    }
            }
                if (!categories.isEmpty()) {
                    if (importImpl(meta.getCompanyId(),
                            categories.stream().map(Category::toEvent).collect(Collectors.toList()), meta)) {
                        meta.setLastImportedCategoryTime(categories.get(categories.size() - 1).getCreationDate());
                    }
                }
            recommendationMetadataRepository.save(meta);
            logger.debug("Added new events and updated meta to {}", meta);
        }
        }
    }

    private boolean importImpl(Long companyId, List<Event> events, RecommendationMetadata meta) {
        try {
            eventServerConnector.bulkImport(companyId, events);
        } catch (IOException | IllegalArgumentException e) {
            if (events.get(0).getEntityId().startsWith("i")) {
                logger.error("Cannot import products from {} - {}.", meta.getLastImportedProductTime(), e.getMessage());
                logger.debug(e.getMessage(), e);
            } else {
                logger.error("Cannot import categories from {} - {}.", meta.getLastImportedCategoryTime(),
                        e.getMessage());
                logger.debug(e.getMessage(), e);
            }
            return false;
        }
        return true;
    }
}
