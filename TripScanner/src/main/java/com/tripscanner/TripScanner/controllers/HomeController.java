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


        // Show the most popular destinations
        ArrayList<Destination> destinations = (ArrayList<Destination>) destinationService.findAll();
        for (int i = 0; i < 5; i++) {
            model.addAttribute("popularDestination", destinations.get(i));
        }
        model.addAttribute("destination", destinationService.findAll());
        model.addAttribute("place", placeService.findAll());
        model.addAttribute("itinerary", itineraryService.findAll());
        return "home";
    }


}
