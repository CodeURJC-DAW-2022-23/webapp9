package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Review;
import com.tripscanner.TripScanner.model.User;
import org.springframework.data.domain.Page;

public class UserDetailsDTO {

    private User user;

    private Page<Itinerary> itineraries;

    private Page<Review> reviews;

    public UserDetailsDTO(User user, Page<Itinerary> itineraries, Page<Review> reviews) {
        this.user = user;
        this.itineraries = itineraries;
        this.reviews = reviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Page<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(Page<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public Page<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Page<Review> reviews) {
        this.reviews = reviews;
    }
}
