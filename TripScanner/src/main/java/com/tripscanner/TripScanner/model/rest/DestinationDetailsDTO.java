package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;

public class DestinationDetailsDTO {

    private Destination destination;

    private Page<Place> places;

    public DestinationDetailsDTO(Destination destination, Page<Place> places) {
        this.destination = destination;
        this.places = places;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Page<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Page<Place> places) {
        this.places = places;
    }

}
