package com.scrum.retrospective.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scrum.retrospective.model.FeedbackItem;
import com.scrum.retrospective.model.FeedbackType;
import com.scrum.retrospective.model.Retrospective;

public class RetrospectiveServiceImplTest {

	@Autowired
    private RetrospectiveService retrospectiveService;
    private Map<String, Retrospective> retrospectives;

    @Before
    public void setUp() {
        retrospectiveService = new RetrospectiveServiceImpl();
        retrospectives = new ConcurrentHashMap<>();
        ((RetrospectiveServiceImpl) retrospectiveService).setRetrospectives(retrospectives);

        // Create a Retrospective with sample data
        Retrospective retrospective1 = new Retrospective();
        retrospective1.setName("Retrospective 1");
        retrospective1.setSummary("Post release retrospective");
        retrospective1.setDate(new Date(1658880000000L)); // 27/07/2022 in milliseconds
        retrospective1.setParticipants(Arrays.asList("Viktor", "Gareth", "Mike"));

        FeedbackItem feedbackItem1 = new FeedbackItem();
        feedbackItem1.setName("Gareth");
        feedbackItem1.setBody("Sprint objective met");
        feedbackItem1.setType(FeedbackType.positive);

        FeedbackItem feedbackItem2 = new FeedbackItem();
        feedbackItem2.setName("Viktor");
        feedbackItem2.setBody("Too many items piled up in the awaiting QA");
        feedbackItem2.setType(FeedbackType.negative);

        FeedbackItem feedbackItem3 = new FeedbackItem();
        feedbackItem3.setName("Mike");
        feedbackItem3.setBody("We should be looking to start using VS2015");
        feedbackItem3.setType(FeedbackType.idea);

        List<FeedbackItem> feedbackItems = Arrays.asList(feedbackItem1, feedbackItem2, feedbackItem3);
        retrospective1.setFeedbackItems(feedbackItems);

        retrospectives.put("Retrospective 1", retrospective1);
    }

    @Test
    public void testGetAllRetrospectives() {
        // Act: Call the method to be tested
        List<Retrospective> allRetrospectives = retrospectiveService.getAllRetrospectives();

        // Assert: Check if the returned list has the correct size
        assertEquals(1, allRetrospectives.size());
    }

    @Test
    public void testGetRetrospectiveByName() {
        // Act: Call the method to be tested
        Retrospective retrievedRetrospective = retrospectiveService.getRetrospectiveByName("Retrospective 1");

        // Assert: Check if the retrieved retrospective is not null and has the correct name
        assertNotNull(retrievedRetrospective);
        assertEquals("Retrospective 1", retrievedRetrospective.getName());
    }

    @Test
    public void testGetRetrospectivesWithPagination() {
        // Arrange: Add some retrospectives to the map (similar to previous examples)

        // Act: Call the method to be tested
        List<Retrospective> paginatedRetrospectives = retrospectiveService.getRetrospectivesWithPagination(0, 10);

        // Assert: Check if the returned list has the correct size based on pagination criteria
        assertEquals(1, paginatedRetrospectives.size()); // Adjusted the expected size to 1
    }
    
    
    @Test
    public void testSearchRetrospectivesByDate() {
        // Arrange: Add some retrospectives to the map with different dates

        // Act: Call the method to be tested
        Date startDate = new Date(1658880000000L); // 27/07/2022 in milliseconds
        Date endDate = new Date(1658966400000L); // 28/07/2022 in milliseconds
        List<Retrospective> retrospectivesByDate = retrospectiveService.searchRetrospectivesByDate(startDate, endDate);

        // Assert: Check if the returned list contains retrospectives within the specified date range
        assertEquals(1, retrospectivesByDate.size());
    }

    @Test
    public void testSearchRetrospectivesByDate_NoResults() {
        // Arrange: Add some retrospectives to the map with different dates

        // Act: Call the method to be tested with a date range that should not match any retrospectives
        Date startDate = new Date(1659043200000L); // 29/07/2022 in milliseconds
        Date endDate = new Date(1659129600000L); // 30/07/2022 in milliseconds
        List<Retrospective> retrospectivesByDate = retrospectiveService.searchRetrospectivesByDate(startDate, endDate);

        // Assert: Check if the returned list is empty
        assertTrue(retrospectivesByDate.isEmpty());
    }
}
