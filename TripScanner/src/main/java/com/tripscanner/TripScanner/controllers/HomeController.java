package com.tripscanner.TripScanner.controllers;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Controller
public class HomeController {
    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/home")
    public String showHomePage(Model model) {

        ArrayList<Destination> destinations = (ArrayList<Destination>) destinationService.findAll();

        // Sort destinations by views (rating)
        Collections.sort(destinations, new Comparator<Destination>() {
            @Override
            public int compare(Destination o1, Destination o2) {
                if (o1.getViews() < o2.getViews()) {
                    return 1;
                } else if (o1.getViews() > o2.getViews()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        // Show the most visited destinations
        for (int i = 0; i < 5; i++) {
            model.addAttribute("popularDestination", destinations.get(i));
        }

        model.addAttribute("destination", destinationService.findAll());
        model.addAttribute("place", placeService.findAll());
        model.addAttribute("itinerary", itineraryService.findAll());

        return "home";
    }


}
