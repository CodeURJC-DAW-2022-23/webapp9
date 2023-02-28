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

    @GetMapping("/details/dest/{id}")
    public String showDestination(Model model, @PathVariable long id){
        Optional<Destination> destination = destinationService.findById(id);
        model.addAttribute("item-name", destination.get().getName());
        model.addAttribute("item-description", destination.get().getDescription());
        model.addAttribute("item-img", destination.get().getImageFile());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            places.add(destination.get().getPlaces().get(i));
        }
        model.addAttribute("information", places);
        model.addAttribute("hide", true);

        return "details";
    }

    @GetMapping("/details/place/{id}")
    public String showPlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("item-name", place.get().getName());
        model.addAttribute("item-description", place.get().getDescription());
        model.addAttribute("item-img", place.get().getImageFile());

        List<Information> itineraries = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            itineraries.add(place.get().getItineraries().get(i));
        }
        model.addAttribute("information", itineraries);
        model.addAttribute("hide", true);

        return "details";
    }

    @GetMapping("/details/itin/{id}")
    public String showItinerary(Model model, @PathVariable long id, Pageable pageable){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        model.addAttribute("item-name", itinerary.get().getName());
        model.addAttribute("item-description", itinerary.get().getDescription());
        model.addAttribute("item-img", itinerary.get().getPlaces().get(0).getImageFile());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            places.add(itinerary.get().getPlaces().get(i));
        }
        model.addAttribute("information", places);

        model.addAttribute("hide", false);


        Page<Review> reviews = reviewService.getItinReviews(itinerary, PageRequest.of(0, 10));

        model.addAttribute("review", reviews);

        return "details";
    }

    //sample for executing with no database information
    @GetMapping("/details/")
    public String showDetails(Model model) throws IOException {
        model.addAttribute("item-name", "nombre");
        model.addAttribute("item-description", "descripcion");
        model.addAttribute("item-img", "img");

        List<Information> places = new ArrayList<>();
        Destination destination = new Destination(1000L, "dest", "dest-desc");
        destination.setFlagCode("es");

        for (int i = 0; i < 10; i++){
            Place p = new Place((long) i, "Nombre-"+i, "Description");
            p.setDestination(destination);

            places.add(p);
        }

        model.addAttribute("information", places);
        model.addAttribute("hide", true);

        return "details";
    }
}
