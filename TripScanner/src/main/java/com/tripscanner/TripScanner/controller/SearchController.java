package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Information;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    // Search methods for "view more details" link in main page
    @GetMapping("/search/destination")
    public String showSearchResultDestination(Model model, Pageable pageable) {
        Page<Destination> destination = destinationService.findAll(PageRequest.of(0, 10, Sort.by("name")));
        model.addAttribute("information", destination);
        return "search";
    }

   @GetMapping("/search/place")
    public String showSearchResultPlace(Model model) {
        Pageable placePaged = PageRequest.of(0, 10, Sort.by("name"));
        Page<Place> place = placeService.findAll(placePaged);
        model.addAttribute("information", place);
        return "search";
    }

    @GetMapping("/search/itinerary")
    public String showSearchResultItinerary(Model model) {
        Pageable itineraryPaged = PageRequest.of(0, 10, Sort.by("name"));
        Page<Itinerary> itinerary = itineraryService.findAll(itineraryPaged);
        model.addAttribute("information", itinerary);
        return "search";
    }

    @GetMapping("/search")
    public String showResult(Model model, @PathVariable String name){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        List<Destination> destination = destinationService.findByQuery(name, name, pageable);
        model.addAttribute("information", destination);
        return "search";

    }

    // Global search by word
   /* @GetMapping("/search")
    public String showSearchResultDest(Model model, @PathVariable String name) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        List<Destination> destination = destinationService.findByQuery(name, name, pageable);
        List<Place> place = placeService.findByQuery(name, name, pageable);
        List<Itinerary> itinerary = itineraryService.findByQuery(name, name, pageable);
        if (!destination.isEmpty()) {
            model.addAttribute("information", destination);
            return "search";
        } else if (!place.isEmpty()) {
            model.addAttribute("information", place);
            return "search";
        } else if (!itinerary.isEmpty()) {
            model.addAttribute("information", itinerary);
            return "search";
        } else {
            return "Not found";
        }
    }*/
}





