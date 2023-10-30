package com.scrum.retrospective.requests;

import java.util.List;

public class CreateRetrospectiveRequest {
    // Fields to hold the information needed to create a retrospective
    private String name;            // Name of the retrospective
    private String summary;         // Summary of the retrospective
    private String date;            // Date of the retrospective (in string format)
    private List<String> participants; // List of participants' names

    // Default constructor
    public CreateRetrospectiveRequest() {
    }

    // Constructor with all fields
    public CreateRetrospectiveRequest(String name, String summary, String date, List<String> participants) {
        this.name = name;
        this.summary = summary;
        this.date = date;
        this.participants = participants;
    }

    // Getter for the name field
    public String getName() {
        return name;
    }

    // Setter for the name field
    public void setName(String name) {
        this.name = name;
    }

    // Getter for the summary field
    public String getSummary() {
        return summary;
    }

    // Setter for the summary field
    public void setSummary(String summary) {
        this.summary = summary;
    }

    // Getter for the date field
    public String getDate() {
        return date;
    }

    // Setter for the date field
    public void setDate(String date) {
        this.date = date;
    }

    // Getter for the participants field
    public List<String> getParticipants() {
        return participants;
    }

    // Setter for the participants field
    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}