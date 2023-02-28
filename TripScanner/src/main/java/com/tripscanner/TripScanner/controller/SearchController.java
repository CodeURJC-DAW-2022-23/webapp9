package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public class SearchController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/search")
    public Object showSearchResultDest(Model model, @PathVariable String name){
        List<Destination> destination = destinationService.findByQuery(name, name, Sort.by("name"));
        if (!destination.isEmpty()) {
            model.addAttribute("destination", destination);
            return "search";
        } else {
            return "This destination is not found";
        }
    }

    @GetMapping("/search")
    public Object showSearchResultPlace(Model model, @PathVariable String name) {
        List<Place> place = placeService.findByQuery(name, name, Sort.by("name"));
        if (!place.isEmpty()) {
            model.addAttribute("place", place);
            return "search";
        } else {
            return "This place is not found";
        }
    }


    @GetMapping("/search")
    public Object showSearchResultItinerary(Model model, @PathVariable String name) {
        List<Itinerary> itinerary = itineraryService.findByQuery(name, name, Sort.by("name"));
        if (!itinerary.isEmpty()) {
            model.addAttribute("itinerary", itinerary);
            return "search";
        } else {
            return "This itinerary is not found";
        }
    }
}
