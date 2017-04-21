/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.services;

import io.prediction.Event;
import io.prediction.FutureAPIResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.JsonObject;
import com.repcar.userdata.bean.EntityType;
import com.repcar.userdata.bean.EventResponse;
import com.repcar.userdata.bean.EventType;
import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.bean.RecommendedContent;
import com.repcar.userdata.repository.RecommendationDetailsRepository;

import feign.FeignException;

@Service
public class EventServerConnector {

    private static final Logger logger = LoggerFactory.getLogger(EventServerConnector.class);

    @Value("${predictionio.eventserver.url}")
    private String eventServerUrl;

    @Autowired
    private EventServer eventServer;

    @Autowired
    private ConnectionsService connections;

    @Autowired
    private RecommendationDetailsRepository recommendationDetailsRepository;

    public String sendEvent(Long companyId, com.repcar.userdata.bean.Event event)
            throws ExecutionException, InterruptedException, IOException, IllegalArgumentException {
        RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);
        if (details == null) {
            throw new IllegalStateException("Recommendation engine not configured for company " + companyId);
        }
        ResponseEntity<EventResponse> response = eventServer.postEvent(details.getApplicationAccessKey(), event);
        logger.debug("Event {} was registered in PIO Event Server with id {}.", event, response.getBody().getEventId());
        return response.getBody().getEventId();
    }

    public com.repcar.userdata.bean.Event getEvent(Long companyId, String entityId) throws RuntimeException {
        try {
            RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);

            if (details != null && details.getApplicationAccessKey() != null) {
                return eventServer.getEvent(entityId, EventType.view, details.getApplicationAccessKey(), true, 1,
                        EntityType.user, EntityType.item).getBody().iterator().next();
            } else {
                throw new IllegalArgumentException("No event server access key provided for company:" + companyId);
            }
        } catch (RuntimeException e) {
            if (e.getCause() instanceof FeignException) {
                throw (FeignException) e.getCause();
            }
            throw e;
        }
    }

    public void replace(Long companyId, String oldEntityId, String newEntityId)
            throws IOException, IllegalArgumentException {
        try {
            RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);

            if (details == null || details.getApplicationAccessKey() == null) {
                throw new IllegalArgumentException("No event server access key provided for company:" + companyId);
            }
            bulkImport(companyId, eventServer.getEvents(oldEntityId, details.getApplicationAccessKey(), -1).getBody()
                    .stream().map(e -> e.entityId(newEntityId)).collect(Collectors.toList()));
            logger.info("Successfully replaces weakId {} with the new one {}.", oldEntityId, newEntityId);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof FeignException) {
                throw (FeignException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    void bulkImport(Long companyId, Collection<Event> events) throws IllegalArgumentException, IOException {
        if (!events.isEmpty()) {
            if (!connections.getEventClients().containsKey(companyId)) {
                connections.addEventClient(companyId);
            }
            // predictionio can do bulk import for max or 50 events
            Iterator<List<Event>> iterator = Iterables.partition(events, 50).iterator();
            while (iterator.hasNext()) {
                List<Event> chunk = iterator.next();
                logger.info("Importing events {}", chunk);
                final FutureAPIResponse response = connections.getEventClients().get(companyId)
                        .createEventsAsFuture(chunk);
                response.addListener(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("Import status {} - {}", response.getStatus(), response.getMessage());
                        if(response.getStatus() == 401){
                            throw new IllegalArgumentException(response.getMessage());
                        }
                    }
                }, MoreExecutors.directExecutor());
            }
        }
    }

    public JsonObject getRecommendation(Long companyId, String weakId, RecommendedContent contentType)
            throws ExecutionException, InterruptedException, IOException {
        JsonObject result = null;
        if (contentType.equals(RecommendedContent.product)) {
            if (!connections.getProductEngines().containsKey(companyId)) {
                connections.addProductEngine(companyId);
            }
            result = connections.getProductEngines().get(companyId).sendQuery(Collections.singletonMap("user", weakId));
        } else {
            if (!connections.getCategoryEngines().containsKey(companyId)) {
                connections.addCategoryEngine(companyId);
            }
            result = connections.getCategoryEngines().get(companyId)
                    .sendQuery(Collections.singletonMap("user", weakId));
        }
        return result;
    }
}
