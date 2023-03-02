package com.tripscanner.TripScanner.controller;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.*;
import com.tripscanner.TripScanner.service.ReviewService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;

@Controller
public class DetailsController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/details/destination/{id}")
    public String showDestination(Model model, @PathVariable long id){
        Optional<Destination> destination = destinationService.findById(id);
        model.addAttribute("item", destination.get());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < Math.min(3, destination.get().getPlaces().size()); i++){
            places.add(destination.get().getPlaces().get(i));
        }
        model.addAttribute("information", places);
        model.addAttribute("hide", true);

        return "details";
    }

    @GetMapping("/details/place/{id}")
    public String showPlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("item", place.get());

        List<Information> itineraries = new ArrayList<>();
        for (int i = 0; i < Math.min(3, place.get().getItineraries().size()); i++){
            itineraries.add(place.get().getItineraries().get(i));
        }
        model.addAttribute("information", itineraries);
        model.addAttribute("hide", true);

        return "details";
    }

    @GetMapping("/details/itinerary/{id}")
    public String showItinerary(Model model, @PathVariable long id, Pageable pageable){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        model.addAttribute("item", itinerary.get());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < Math.min(3, itinerary.get().getPlaces().size()); i++){
            places.add(itinerary.get().getPlaces().get(i));
        }
        model.addAttribute("information", places);

        model.addAttribute("hide", false);

        Page<Review> reviews = reviewService.getItinReviews(itinerary, PageRequest.of(0, 10));
        model.addAttribute("review", reviews);
        /* Replace at security merge
         * model.addAttribute("isLogged", request.getUserPrincipal() != null);
         */
        model.addAttribute("isLogged", true);

        return "details";
    }
}
