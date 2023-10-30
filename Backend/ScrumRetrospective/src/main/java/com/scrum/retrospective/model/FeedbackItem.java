package com.scrum.retrospective.model;

public class FeedbackItem {
    private String name;
    private String body;
    private FeedbackType type;
    
    
    public FeedbackItem() {
       
    }

    public FeedbackItem(String name, String body, FeedbackType type) {
        this.name = name;
        this.body = body;
        this.type = type;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FeedbackType getType() {
        return type;
    }

    public void setType(FeedbackType type) {
        this.type = type;
    }
}