package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;

public class ItineraryDetails {

    private Itinerary itinerary;

    private Page<Place> places;

    public ItineraryDetails(Itinerary itinerary, Page<Place> places) {
        this.itinerary = itinerary;
        this.places = places;
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

}
