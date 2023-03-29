package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.Review;
import org.springframework.data.domain.Page;

public class ItineraryDetailsDTO {

    private Itinerary itinerary;

    private Page<Place> places;

    private Page<Review> reviews;

    public ItineraryDetailsDTO(Itinerary itinerary, Page<Place> places, Page<Review> reviews) {
        this.itinerary = itinerary;
        this.places = places;
        this.reviews = reviews;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Page<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Page<Place> places) {
        this.places = places;
    }

    public Page<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Page<Review> reviews) {
        this.reviews = reviews;
    }
}
