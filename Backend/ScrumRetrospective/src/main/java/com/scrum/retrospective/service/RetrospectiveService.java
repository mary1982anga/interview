package com.scrum.retrospective.service;

import com.scrum.retrospective.model.FeedbackItem;
import com.scrum.retrospective.model.Retrospective;
import com.scrum.retrospective.service.RetrospectiveService;

import java.util.Date;
import java.util.List;

public interface RetrospectiveService {
	
	//returns a list of all retrospectives.
	List<Retrospective> getAllRetrospectives();

	//retrieves a retrospective by its name
    Retrospective getRetrospectiveByName(String name);

    //adds a new retrospective
    void addRetrospective(Retrospective retrospective);

    //adds a feedback item to a retrospective.
    void addFeedbackToRetrospective(String name, FeedbackItem feedbackItem);

    
    //Update the body and type of a feedback item in a retrospective.
	void updateFeedbackItem(String name, int feedbackItemId, FeedbackItem updatedFeedbackItem);
	
	//Search Retrospectives

	List<Retrospective> getRetrospectivesWithPagination(int currentPage, int pageSize);

	List<Retrospective> searchRetrospectivesByDate(Date start, Date end);
	
  
	
}