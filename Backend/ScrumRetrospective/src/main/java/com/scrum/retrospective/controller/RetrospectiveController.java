package com.scrum.retrospective.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.scrum.retrospective.exception.FeedbackItemNotFoundException;
import com.scrum.retrospective.exception.InvalidRetrospectiveException;
import com.scrum.retrospective.exception.RetrospectiveNotFoundException;
import com.scrum.retrospective.model.FeedbackItem;
import com.scrum.retrospective.model.Retrospective;
import com.scrum.retrospective.requests.CreateRetrospectiveRequest;

import java.text.ParseException;
import com.scrum.retrospective.service.RetrospectiveService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private static final Logger LOGGER = Logger.getLogger(RetrospectiveController.class.getName());

    @Autowired
    private RetrospectiveService retrospectiveService;

    // Get all retrospectives
    @GetMapping
    public List<Retrospective> getAllRetrospectives() {
        LOGGER.info("Getting all retrospectives.");
        return retrospectiveService.getAllRetrospectives();
    }

    // Get a retrospective by name
    @GetMapping("/{name}")
    public ResponseEntity<Retrospective> getRetrospectiveByName(@PathVariable String name) {
        try {
            Retrospective retrospective = retrospectiveService.getRetrospectiveByName(name);
            if (retrospective != null) {
                return ResponseEntity.ok(retrospective);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting retrospective by name: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create a retrospective
//    @PostMapping
//    public ResponseEntity<Void> createRetrospective(@RequestBody CreateRetrospectiveRequest request) {
//        try {
//            LOGGER.info("Adding retrospective: " + request.getName());
//            
//           
//            // Validate request parameters 
//            if (request.getName() == null || request.getName().isEmpty() || request.getSummary() == null || request.getSummary().isEmpty()) {
//                throw new InvalidRetrospectiveException("Invalid request parameters");
//            }
//
//            // Convert the date string to a Date object
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            Date date = dateFormat.parse(request.getDate());
//
//            // Create and set retrospective details
//            Retrospective retrospective = new Retrospective();
//            retrospective.setName(request.getName());
//            retrospective.setSummary(request.getSummary());
//            retrospective.setDate(date); // Set the Date object
//
//            retrospective.setParticipants(request.getParticipants());
//
//            // Validate retrospective
//            retrospective.validate(); 
//
//            retrospectiveService.addRetrospective(retrospective);
//
//            return ResponseEntity.status(HttpStatus.CREATED).build();
//        } catch (ParseException e) {
//            // Handle date parsing error
//            LOGGER.log(Level.WARNING, "Error parsing date: {0}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (InvalidRetrospectiveException e) {
//            LOGGER.log(Level.WARNING, "Invalid retrospective: {0}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error adding retrospective: {0}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//   

    @PostMapping
    public ResponseEntity<Void> createRetrospective(@RequestBody CreateRetrospectiveRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            LOGGER.info("Adding retrospective: " + request.getName());
            
            if (request == null) 
                throw new InvalidRetrospectiveException("Invalid request: Request cannot be null");

            // Validate request parameters 
            if (request.getName() == null || request.getName().isEmpty() || request.getSummary() == null || request.getSummary().isEmpty()) {
                throw new InvalidRetrospectiveException("Invalid request parameters");
            }

            // Convert the date string to a Date object
            Date date = dateFormat.parse(request.getDate());

            // Create and set retrospective details
            Retrospective retrospective = new Retrospective();
            retrospective.setName(request.getName());
            retrospective.setSummary(request.getSummary());
            retrospective.setDate(date); // Set the Date object

            retrospective.setParticipants(request.getParticipants());

            // Validate retrospective
            retrospective.validate(); 

            retrospectiveService.addRetrospective(retrospective);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ParseException e) {
            // Handle date parsing error
            LOGGER.log(Level.WARNING, "Error parsing date: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InvalidRetrospectiveException e) {
            LOGGER.log(Level.WARNING, "Invalid retrospective: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding retrospective: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    
    // Add feedback to a retrospective
    @PostMapping("/{name}/feedback")
    public ResponseEntity<String> addFeedbackToRetrospective(
            @PathVariable String name,
            @RequestBody FeedbackItem feedbackItem) {
        try {
            LOGGER.info("Adding feedback item to retrospective: " + name);

            Retrospective retrospective = retrospectiveService.getRetrospectiveByName(name);
            if (retrospective == null) {
                throw new RetrospectiveNotFoundException(name);
            }

            retrospective.addFeedbackItem(feedbackItem);
            retrospectiveService.addFeedbackToRetrospective(name, feedbackItem);

            return ResponseEntity.ok("Feedback item added successfully.");
        } catch (RetrospectiveNotFoundException e) {
            LOGGER.log(Level.WARNING, "Retrospective not found: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding feedback item: {0}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding feedback item.");
        }
    }


    // Update feedback item in retrospective
    @PutMapping("/{name}/feedback/{feedbackItemId}")
    public ResponseEntity<String> updateFeedbackItem(
            @PathVariable String name,
            @PathVariable int feedbackItemId,
            @RequestBody FeedbackItem updatedFeedbackItem) {
        try {
            LOGGER.info("Updating feedback item in retrospective: " + name);

            Retrospective retrospective = retrospectiveService.getRetrospectiveByName(name);
            if (retrospective == null) {
                throw new RetrospectiveNotFoundException(name);
            }

            List<FeedbackItem> feedback = retrospective.getFeedbackItems();
            if (feedbackItemId >= 0 && feedbackItemId < feedback.size()) {
                FeedbackItem existingFeedbackItem = feedback.get(feedbackItemId);
                existingFeedbackItem.setBody(updatedFeedbackItem.getBody());
                existingFeedbackItem.setType(updatedFeedbackItem.getType());
            } else {
                throw new FeedbackItemNotFoundException("Feedback item with ID " + feedbackItemId + " not found.");
            }

            return ResponseEntity.ok("Feedback item updated successfully.");
        } catch (RetrospectiveNotFoundException | FeedbackItemNotFoundException e) {
            LOGGER.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating feedback item.");
        }
    }

   
    // Get retrospectives with pagination
    @GetMapping(value = "/pagination", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Retrospective>> getRetrospectivesWithPagination(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            LOGGER.info("Getting retrospectives with pagination. Page: " + currentPage + ", Page Size: " + pageSize);

            List<Retrospective> retrospectives = retrospectiveService.getRetrospectivesWithPagination(currentPage, pageSize);
            return ResponseEntity.ok(retrospectives);
        } catch (Exception e) {
            LOGGER.severe("An error occurred while retrieving retrospectives: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get retrospectives with optional date range filter
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Retrospective>> getRetrospectives(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date start = startDate != null ? dateFormat.parse(startDate) : null;
            Date end = endDate != null ? dateFormat.parse(endDate) : null;

            List<Retrospective> retrospectives = retrospectiveService.searchRetrospectivesByDate(start, end);
            return ResponseEntity.ok(retrospectives);
        } catch (ParseException e) {
            LOGGER.warning("Invalid date format provided.");
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            LOGGER.severe("An error occurred while retrieving retrospectives: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}