package com.thinkincode.events_android.model;

public class Task {

    private String id;
    private String name;
    private String category;
    private String _for;
    private String responsible;
    private boolean complete;

    public Task(String id, String name, String category, String _for, String responsible, boolean complete) {
        this.id = id;
        this.name = name;
        this.category = category;
        this._for = _for;
        this.responsible = responsible;
        this.complete = complete;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String get_for() {
        return _for;
    }

    public void set_for(String _for) {
        this._for = _for;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

}
