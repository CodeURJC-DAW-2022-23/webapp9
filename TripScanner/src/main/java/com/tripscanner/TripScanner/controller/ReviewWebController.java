package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Review;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.ReviewService;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class ReviewWebController {

    @Autowired
    UserService userService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ItineraryService itineraryService;

    @PostMapping("/reviews/add/{id}")
    public String newReviewProcess(Model model, HttpServletRequest request, Review review, @PathVariable long id) {

        if (review.getScore() > 5 || review.getScore() < 0) return "redirect:/details/itinerary/"+id;

        /* Replace at security merge
         * userService.findByName(request.getUserPrincipal().getName()).get()
         */
        review.setUser(userService.findByUsername("admin").get());
        review.setViews(0L);
        review.setItinerary(itineraryService.findById(id).get());
        review.setDate(new Date());
        reviewService.save(review);

        return "redirect:/details/itinerary/"+id;
    }

}
