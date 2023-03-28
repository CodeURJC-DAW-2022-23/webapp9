package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Destination;

import java.util.List;

public class GraphDetailsDTO {

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

    public GraphDetailsDTO() {
    }

    public GraphDetailsDTO(List<Destination> destinations, List<Long> views) {
        this.destinations = destinations;
        this.views = views;
    }
}
