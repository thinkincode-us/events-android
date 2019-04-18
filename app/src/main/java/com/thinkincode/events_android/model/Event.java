package com.thinkincode.events_android.model;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String id;
    private String parentId;
    private String name;
    private String entityId;
    private String entityName;
    private List<Task> tasks;





    public Event(String id, String name, String entityId, String entityName) {

        this.id = id;
        this.name = name;
        this.entityId = entityId;
        this.entityName = entityName;
    }


    public Event() {

    }


    public void addTasks(Task task) {
        if (tasks == null) tasks = new ArrayList<>();

        this.tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getParentId() {
        return parentId;
    }
    /*
    private String id;
    private String name;

    public String getEventCatalogId() {
        return id;
    }

    public void setEventCatalogId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    */
}
