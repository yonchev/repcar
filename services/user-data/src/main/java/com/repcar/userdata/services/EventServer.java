/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.services;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import io.prediction.Event;

import java.util.Collection;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.repcar.userdata.bean.EntityType;
import com.repcar.userdata.bean.EventResponse;
import com.repcar.userdata.bean.EventType;

/**
 * REST client to PredictionIO's Event Server.
 * 
 * @author <a href="mailto:tihomir.slavkov@repcarpro.com">Tihomir Slavkov</a>
 *
 */
@FeignClient(url = "${predictionio.eventserver.url}", name = "user-data")
public interface EventServer {

    /**
     * Gets the most recent event of type {@link EventType.view} for entityType {@link EntityType.user} and target
     * entity type {@link EntityType.item} i.e. gets the most recently viewed product by an user.
     * 
     * @param entityId
     *            the id of the entity. It must be a weakId.
     * @param event
     *            the event type. It must be {@link EventType.view}
     * @param accessKey
     *            the access key for the application in the Event Server.
     * @param reversed
     *            whether to return the first (false) or last (true) event. It must be true.
     * @param limit
     *            the number of events to return. It must be 1.
     * @param entityType
     *            the entity which performed the event. It must be {@link EntityType.user}
     * @param targetEntityType
     *            the target entity of the event. It must be {@link EntityType.item}
     * @return the event about the most recently viewed product by an user.
     * @throws RuntimeException
     *             on exception or when there isn't such event.
     */
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE, path = "/events.json", params = { "reversed=true",
            "limit=1", "entityType=user", "event=view", "targetEntityType=item" })
    ResponseEntity<Collection<com.repcar.userdata.bean.Event>> getEvent(
            @RequestParam(name = "entityId") String entityId, @RequestParam(name = "event") EventType event,
            @RequestParam(name = "accessKey") String accessKey, @RequestParam(name = "reversed") boolean reversed,
            @RequestParam(name = "limit") int limit, @RequestParam(name = "entityType") EntityType entityType,
            @RequestParam(name = "targetEntityType") EntityType targetEntityType) throws RuntimeException;

    /**
     * Gets the most recent event of type {@link EventType.view} for entityType {@link EntityType.user} and target
     * entity type {@link EntityType.item} i.e. gets the most recently viewed product by an user.
     * 
     * @param entityId
     *            the id of the entity. It must be a weakId.
     * @param accessKey
     *            the access key for the application in the Event Server.
     * @param limit
     *            the number of events to return. It must be -1.
     * @return the event about the most recently viewed product by an user.
     * @throws RuntimeException
     *             on exception or when there isn't such event.
     */
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE, path = "/events.json", params = { "limit=-1" })
    ResponseEntity<Collection<Event>> getEvents(@RequestParam(name = "entityId") String entityId,
            @RequestParam(name = "accessKey") String accessKey, @RequestParam(name = "limit") int limit)
            throws RuntimeException;

    @RequestMapping(method = POST, produces = APPLICATION_JSON_VALUE, path = "/events.json")
    ResponseEntity<EventResponse> postEvent(@RequestParam(name = "accessKey") String accessKey,
            @RequestBody com.repcar.userdata.bean.Event event);
}
