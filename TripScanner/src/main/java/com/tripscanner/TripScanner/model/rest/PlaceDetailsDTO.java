package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;

public class PlaceDetailsDTO {

    private Place place;

    private Page<Itinerary> itineraries;

    public PlaceDetailsDTO(Place place, Page<Itinerary> itineraries) {
        this.place = place;
        this.itineraries = itineraries;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Page<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(Page<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }
}
