package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagementController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @GetMapping("/management/destinations")
    public String showManagementDest(Model model, Pageable pageable){
        model.addAttribute("hasFlag", true);
        model.addAttribute("userItems", false);
        Page<Destination> dest = destinationService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", dest);
        model.addAttribute("type", "Destination");
        return "management";
    }
    @GetMapping("/management/users")
    public String showManagementUser(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("items", false);
        Page<User> users = userService.findAll(PageRequest.of(0,10));
        model.addAttribute("userItems", users);
        model.addAttribute("type", "User");
        return "management";
    }
    @GetMapping("/management/itineraries")
    public String showManagementItin(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("userItems", false);
        Page<Itinerary> itin = itineraryService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", itin);
        model.addAttribute("type", "Itinerary");
        return "management";
    }
    @GetMapping("/management/places")
    public String showManagementPlace(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("userItems", false);
        Page<Place> places = placeService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", places);
        model.addAttribute("type", "Place");
        return "management";
    }
}
