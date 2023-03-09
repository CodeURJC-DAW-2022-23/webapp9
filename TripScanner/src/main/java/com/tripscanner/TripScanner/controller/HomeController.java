package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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

        // Set id of most visited destination
        Page<Destination> topDestination = destinationService.findAll(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "views")));
        model.addAttribute("destination-id", topDestination.get().findFirst().get().getId());

        // Set id of most visited place
        Page<Place> topPlace = placeService.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "views")));
        model.addAttribute("place-id", topPlace.get().findFirst().get().getId());

        // Set id of most visited itinerary
        Page<Itinerary> topItinerary = itineraryService.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "views")));
        model.addAttribute("itinerary-id", topItinerary.get().findFirst().get().getId());


        // Show 5 the most visited destinations
        Page<Destination> popularDestination = destinationService.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views")));
        model.addAttribute("popularDestination", popularDestination);

        // Show 3 cards with possible destinations
        Pageable destinationsPaged = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));
        Page<Destination> destinations = destinationService.findAll(destinationsPaged);
        model.addAttribute("destinations", destinations);

        //Show 3 cards with possible places
        Pageable placePaged = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));
        Page<Place> place = placeService.findAll(placePaged);
        model.addAttribute("place", place);

        //Show 3 cards with possible itinerary
        Pageable itineraryPaged = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"));
        Page<Itinerary> itinerary = itineraryService.findAll(itineraryPaged);
        model.addAttribute("itinerary", itinerary);

        return "index";
    }



}
