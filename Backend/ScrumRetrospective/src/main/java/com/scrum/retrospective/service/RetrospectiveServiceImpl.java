package com.scrum.retrospective.service;

import com.scrum.retrospective.model.FeedbackItem;
import com.scrum.retrospective.model.Retrospective;
import com.scrum.retrospective.exception.RetrospectiveNotFoundException;
import com.scrum.retrospective.exception.RetrospectiveServiceException;
import org.springframework.stereotype.Service;
import java.util.logging.Logger; // Import the Logger class
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Service
public class RetrospectiveServiceImpl implements RetrospectiveService {
    private static final Logger LOGGER = Logger.getLogger(RetrospectiveServiceImpl.class.getName());

    // A map to store retrospectives with their names as keys
    private Map<String, Retrospective> retrospectives = new ConcurrentHashMap<>();

   
    @Override
    public List<Retrospective> getAllRetrospectives() {
        return new ArrayList<>(retrospectives.values());
    }

    @Override
    public Retrospective getRetrospectiveByName(String name) {
        return retrospectives.get(name);
    }

    @Override
    public void addRetrospective(Retrospective retrospective) {
        retrospectives.put(retrospective.getName(), retrospective);
    }

    @Override
    public void addFeedbackToRetrospective(String name, FeedbackItem feedbackItem) {
        Retrospective retrospective = getRetrospectiveByName(name);
        if (retrospective != null) {
            List<FeedbackItem> feedback = retrospective.getFeedbackItems();
            feedback.add(feedbackItem);
            retrospective.setFeedbackItems(feedback);
        }
    }

    @Override
    public void updateFeedbackItem(String name, int feedbackItemId, FeedbackItem updatedFeedbackItem) {
        Retrospective retrospective = getRetrospectiveByName(name);
        if (retrospective != null) {
            List<FeedbackItem> feedback = retrospective.getFeedbackItems();
            if (feedbackItemId >= 0 && feedbackItemId < feedback.size()) {
                FeedbackItem existingFeedbackItem = feedback.get(feedbackItemId);
                existingFeedbackItem.setBody(updatedFeedbackItem.getBody());
                existingFeedbackItem.setType(updatedFeedbackItem.getType());
            }
        }
    }

    @Override
    public List<Retrospective> getRetrospectivesWithPagination(int currentPage, int pageSize) {
        try {
            int startIndex = currentPage * pageSize;
            List<Retrospective> allRetrospectives = new ArrayList<>(retrospectives.values());
            int endIndex = Math.min(startIndex + pageSize, allRetrospectives.size());
            return allRetrospectives.subList(startIndex, endIndex);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while getting retrospectives with pagination: {0}", e.getMessage());
            throw new RetrospectiveServiceException("Error while getting retrospectives with pagination.", e);
        }
    }

    @Override
    public List<Retrospective> searchRetrospectivesByDate(Date startDate, Date endDate) {
        List<Retrospective> filteredRetrospectives = new ArrayList<>();

        for (Retrospective retrospective : retrospectives.values()) {
            Date retrospectiveDate = retrospective.getDate();

            if ((startDate == null || retrospectiveDate.after(startDate) || retrospectiveDate.equals(startDate))
                    && (endDate == null || retrospectiveDate.before(endDate) || retrospectiveDate.equals(endDate))) {
                filteredRetrospectives.add(retrospective);
            }
        }

        return filteredRetrospectives;
    }

    public void setRetrospectives(Map<String, Retrospective> retrospectives) {
        this.retrospectives = retrospectives;
    }

//	@Override
//	public void addFeedbackItemToRetrospective(String retrospectiveName, FeedbackItem feedbackItem) {
//		// Retrieve the retrospective by name
//	    Retrospective retrospective = retrospectives.get(retrospectiveName);
//	    
//	    if (retrospective != null) {
//	        // Add the feedback item to the retrospective
//	        List<FeedbackItem> feedbackItems = retrospective.getFeedbackItems();
//	        feedbackItems.add(feedbackItem);
//	        retrospective.setFeedbackItems(feedbackItems);
//	    } else {
//	        // Handle the case where the retrospective is not found
//	        throw new RetrospectiveNotFoundException("Retrospective with name " + retrospectiveName + " not found.");
//	    }
//		
//	}
}