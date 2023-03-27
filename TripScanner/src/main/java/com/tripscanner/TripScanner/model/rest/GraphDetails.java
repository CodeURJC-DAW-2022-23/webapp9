package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;

import java.util.List;

public class GraphDetails {

    private List<Destination> destinations;

    private List<Long> views;

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Long> getViews() {
        return views;
    }

    public void setViews(List<Long> views) {
        this.views = views;
    }

    public GraphDetails() {
    }

    public GraphDetails(List<Destination> destinations, List<Long> views) {
        this.destinations = destinations;
        this.views = views;
    }
}
