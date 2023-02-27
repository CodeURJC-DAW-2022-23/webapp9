package com.tripscanner.TripScanner.controlller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public class SearchController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/search/dest-{id}")
    public Object showSearchResultDest(Model model, @PathVariable String name){
        Optional<Destination> destination = destinationService.findByName(name);
        if (destination.isPresent()) {
            model.addAttribute("destination", destination.get().getId());
            return "search";
        } else {
            return "This destination is not found";
        }
    }

    @GetMapping("/search/place-{id}")
    public Object showSearchResultPlace(Model model, @PathVariable String name) {
        Optional<Place> place = placeService.findByName(name);
        if (place.isPresent()) {
            model.addAttribute("place", place.get().getId());
            return "search";
        } else {
            return "This place is not found";
        }
    }



    @GetMapping("/search/itinerary-{id}")
    public Object showSearchResultItinerary(Model model, @PathVariable String name) {
        Optional<Itinerary> itinerary = itineraryService.findByName(name);
        if (itinerary.isPresent()) {
            model.addAttribute("itinerary", itinerary.get().getId());
            return "search";
        } else {
            return "This itinerary is not found";
        }
    }
}
