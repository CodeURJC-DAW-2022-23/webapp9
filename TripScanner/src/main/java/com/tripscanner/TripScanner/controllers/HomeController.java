package com.tripscanner.TripScanner.controllers;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/")
    public String showHomePage(Model model) {

        List<Destination> popularDestination = destinationService.findAll(Sort.by("views"));

        // Show the most visited destinations
        for (int i = 0; i < 5; i++) {
            model.addAttribute("popularDestination", popularDestination.get(i));
        }

        // Show 3 cards with possible destinations
        ArrayList<Destination> dest = (ArrayList<Destination>) destinationService.findAll();
        for (int i = 0; i < 5; i++) {
            model.addAttribute("destinations", dest.get(i));
        }

        //Show 3 cards with possible places
        ArrayList<Place> place = (ArrayList<Place>) placeService.findAll();
        for (int i = 0; i < 5; i++) {
            model.addAttribute("place", place.get(i));
        }

        //Show 3 cards with possible itinerary
        ArrayList<Itinerary> itinerary = (ArrayList<Itinerary>) itineraryService.findAll();
        for (int i = 0; i < 5; i++) {
            model.addAttribute("itinerary", itinerary.get(i));
        }

        return "index";
    }


}
