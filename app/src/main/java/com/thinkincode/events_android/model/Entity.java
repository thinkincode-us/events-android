package com.thinkincode.events_android.model;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String name;
    private String id;
    public static List<Entity> data=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
