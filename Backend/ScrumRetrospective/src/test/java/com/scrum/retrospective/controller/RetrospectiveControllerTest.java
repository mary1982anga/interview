package com.scrum.retrospective.controller;

import java.util.Arrays;
import java.util.List;

import com.scrum.retrospective.controller.RetrospectiveController;
import com.scrum.retrospective.exception.RetrospectiveServiceException;
import com.scrum.retrospective.model.FeedbackItem;
import com.scrum.retrospective.model.FeedbackType;
import com.scrum.retrospective.model.Retrospective;
import com.scrum.retrospective.requests.CreateRetrospectiveRequest;
import com.scrum.retrospective.service.RetrospectiveService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RetrospectiveControllerTest {

    @Mock
    private RetrospectiveService retrospectiveService;

    @InjectMocks
    private RetrospectiveController retrospectiveController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRetrospectivesWithPagination() {
        List<Retrospective> retrospectives = Arrays.asList(new Retrospective(), new Retrospective());
        when(retrospectiveService.getRetrospectivesWithPagination(0, 10)).thenReturn(retrospectives);

        ResponseEntity<List<Retrospective>> responseEntity = retrospectiveController.getRetrospectivesWithPagination(0, 10);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(retrospectives, responseEntity.getBody());
    }

    @Test
    public void testCreateRetrospective() {
        CreateRetrospectiveRequest request = new CreateRetrospectiveRequest();
        request.setName("NewRetrospective");
        request.setSummary("Sample summary");
        request.setDate("01/01/2023");
        request.setParticipants(Arrays.asList("John", "Jane"));

        ResponseEntity<Void> responseEntity = retrospectiveController.createRetrospective(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(retrospectiveService, times(1)).addRetrospective(any(Retrospective.class));
    }

    @Test
    public void testCreateRetrospective_InvalidRequest() {
        ResponseEntity<Void> responseEntity = retrospectiveController.createRetrospective(null);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testGetRetrospectiveByName() {
        Retrospective retrospective = new Retrospective();
        retrospective.setName("Retrospective 1");
        when(retrospectiveService.getRetrospectiveByName("Retrospective 1")).thenReturn(retrospective);

        ResponseEntity<Retrospective> responseEntity = retrospectiveController.getRetrospectiveByName("Retrospective 1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(retrospective, responseEntity.getBody());
    }

    @Test
    public void testGetRetrospectiveByName_NotFound() {
        when(retrospectiveService.getRetrospectiveByName("NonExistentRetrospective")).thenReturn(null);

        ResponseEntity<Retrospective> responseEntity = retrospectiveController.getRetrospectiveByName("NonExistentRetrospective");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddFeedbackToRetrospective() {
        // Arrange
        Retrospective retrospective = new Retrospective();
        when(retrospectiveService.getRetrospectiveByName("Retrospective 1")).thenReturn(retrospective);

        FeedbackItem feedbackItem = new FeedbackItem();
        feedbackItem.setName("John");
        feedbackItem.setBody("Good progress");
        feedbackItem.setType(FeedbackType.positive);

        // Act
        ResponseEntity<String> responseEntity = retrospectiveController.addFeedbackToRetrospective("Retrospective 1", feedbackItem);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(retrospectiveService, times(1)).getRetrospectiveByName("Retrospective 1");
        verify(retrospectiveService, times(1)).addFeedbackToRetrospective(eq("Retrospective 1"), any(FeedbackItem.class));
     
    }

    @Test
    public void testAddFeedbackToRetrospective_NotFound() {
        when(retrospectiveService.getRetrospectiveByName("NonExistentRetrospective")).thenReturn(null);

        ResponseEntity<String> responseEntity = retrospectiveController.addFeedbackToRetrospective("NonExistentRetrospective", new FeedbackItem());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testAddFeedbackToRetrospective_Exception() {
        when(retrospectiveService.getRetrospectiveByName("Retrospective 1")).thenThrow(new RetrospectiveServiceException("Test Exception"));

        ResponseEntity<String> responseEntity = retrospectiveController.addFeedbackToRetrospective("Retrospective 1", new FeedbackItem());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateFeedbackItem() {
        // Create a mock FeedbackItem
        FeedbackItem feedbackItem = new FeedbackItem();

        // Call the method being tested
        ResponseEntity<String> responseEntity = retrospectiveController.updateFeedbackItem("Retrospective 1", 0, feedbackItem);

        // Verify that the method was called with the correct arguments
      //  verify(retrospectiveService, times(1)).updateFeedbackItem("Retrospective 1", 0, feedbackItem);
     
        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateFeedbackItem_RetrospectiveNotFound() {
        when(retrospectiveService.getRetrospectiveByName("NonExistentRetrospective")).thenReturn(null);

        ResponseEntity<String> responseEntity = retrospectiveController.updateFeedbackItem("NonExistentRetrospective", 0, new FeedbackItem());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    
@Test
public void testUpdateFeedbackItem_Exception() {
    when(retrospectiveService.getRetrospectiveByName("Retrospective 1")).thenThrow(new RetrospectiveServiceException("Test Exception"));

    ResponseEntity<String> responseEntity = retrospectiveController.updateFeedbackItem("Retrospective 1", 0, new FeedbackItem());

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
}

}