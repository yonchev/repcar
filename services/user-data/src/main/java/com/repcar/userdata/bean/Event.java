/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.bean;

import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class Event {

    private String eventId;
    @NotNull
    private EventType event;
    @NotNull
    private EntityType entityType;
    @NotNull
    private String entityId;
    @NotNull
    private EntityType targetEntityType;
    @NotNull
    private String targetEntityId;

    private String eventTime;

    private Map<String, Object> properties;

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public EntityType getTargetEntityType() {
        return targetEntityType;
    }

    public void setTargetEntityType(EntityType entityType) {
        this.targetEntityType = entityType;
    }

    public Map<String, Object> getProperties() {
        return properties != null ? properties : Collections.EMPTY_MAP;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventDetails [user=").append(entityId).append(", action=").append(event).append(", item=")
                .append(targetEntityId).append(", properties=").append(properties).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        result = prime * result + ((eventTime == null) ? 0 : eventTime.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        result = prime * result + ((targetEntityId == null) ? 0 : targetEntityId.hashCode());
        result = prime * result + ((targetEntityType == null) ? 0 : targetEntityType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (entityId == null) {
            if (other.entityId != null)
                return false;
        } else if (!entityId.equals(other.entityId))
            return false;
        if (entityType != other.entityType)
            return false;
        if (event != other.event)
            return false;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        if (eventTime == null) {
            if (other.eventTime != null)
                return false;
        } else if (!eventTime.equals(other.eventTime))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        if (targetEntityId == null) {
            if (other.targetEntityId != null)
                return false;
        } else if (!targetEntityId.equals(other.targetEntityId))
            return false;
        if (targetEntityType != other.targetEntityType)
            return false;
        return true;
    }

}