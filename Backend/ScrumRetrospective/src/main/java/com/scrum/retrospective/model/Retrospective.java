package com.scrum.retrospective.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Retrospective {
	private int id;
    private String name;
    private String summary;
    private Date date;
    private List<String> participants;
    private List<FeedbackItem> feedbackItems;

    // Constructors
    public Retrospective() {
        // Default constructor
    	this.feedbackItems = new ArrayList<>();
    }

    public Retrospective(int id, String name, String summary, Date date, List<String> participants, List<FeedbackItem> feedbackItems) {
    	this.id = id;
        this.name = name;
        this.summary = summary;
        this.date = date;
        this.participants = participants;
        this.feedbackItems = feedbackItems;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<FeedbackItem>  getFeedbackItems() {
        return feedbackItems;
    }

    public void setFeedbackItems(List<FeedbackItem> feedbackItems) {
        this.feedbackItems = feedbackItems;
    }

    public void addFeedbackItem(FeedbackItem feedbackItem) {
        if (feedbackItems == null) {
            feedbackItems = new ArrayList<>();
        }
        feedbackItems.add(feedbackItem);
    }
    public void validate() {
        if (this.date == null || this.participants == null || this.participants.isEmpty()) {
            throw new IllegalArgumentException("Retrospective must have a date and at least one participant.");
        }
    }

}