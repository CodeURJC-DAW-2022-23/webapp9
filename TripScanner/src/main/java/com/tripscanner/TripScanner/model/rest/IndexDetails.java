package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;

import java.util.List;

public class IndexDetails {

    private Destination topDestination;

    private Itinerary topItinerary;

    private Place topPlace;

    private List<Destination> topDestinations;

    private List<Destination> newestDestinations;

    private List<Itinerary> newestItineraries;

    private List<Place> newestPlaces;

    public IndexDetails(Destination topDestination, Itinerary topItinerary, Place topPlace,
                        List<Destination> topDestinations, List<Destination> newestDestinations,
                        List<Itinerary> newestItineraries, List<Place> newestPlaces) {
        this.topDestination = topDestination;
        this.topItinerary = topItinerary;
        this.topPlace = topPlace;
        this.topDestinations = topDestinations;
        this.newestDestinations = newestDestinations;
        this.newestItineraries = newestItineraries;
        this.newestPlaces = newestPlaces;
    }

    public Destination getTopDestination() {
        return topDestination;
    }

    public void setTopDestination(Destination topDestination) {
        this.topDestination = topDestination;
    }

    public Itinerary getTopItinerary() {
        return topItinerary;
    }

    public void setTopItinerary(Itinerary topItinerary) {
        this.topItinerary = topItinerary;
    }

    public Place getTopPlace() {
        return topPlace;
    }

    public void setTopPlace(Place topPlace) {
        this.topPlace = topPlace;
    }

    public List<Destination> getTopDestinations() {
        return topDestinations;
    }

    public void setTopDestinations(List<Destination> topDestinations) {
        this.topDestinations = topDestinations;
    }

    public List<Destination> getNewestDestinations() {
        return newestDestinations;
    }

    public void setNewestDestinations(List<Destination> newestDestinations) {
        this.newestDestinations = newestDestinations;
    }

    public List<Itinerary> getNewestItineraries() {
        return newestItineraries;
    }

    public void setNewestItineraries(List<Itinerary> newestItineraries) {
        this.newestItineraries = newestItineraries;
    }

    public List<Place> getNewestPlaces() {
        return newestPlaces;
    }

    public void setNewestPlaces(List<Place> newestPlaces) {
        this.newestPlaces = newestPlaces;
    }

}
