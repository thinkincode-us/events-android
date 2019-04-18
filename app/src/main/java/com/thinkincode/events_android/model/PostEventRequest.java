package com.thinkincode.events_android.model;

public class PostEventRequest {

    private String eventCatalogId;
    private String name;
    private String entityId;
    private String entityName;

    public PostEventRequest(String eventCatalogId, String name, String entityId, String entityName) {
        this.eventCatalogId = eventCatalogId;
        this.name = name;
        this.entityId = entityId;
        this.entityName = entityName;
    }

    public String getEventCatalogId() {
        return eventCatalogId;
    }

    public void setEventCatalogId(String eventCatalogId) {
        this.eventCatalogId = eventCatalogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
