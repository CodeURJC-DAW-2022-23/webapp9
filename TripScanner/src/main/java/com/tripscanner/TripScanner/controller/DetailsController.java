package com.tripscanner.TripScanner.controller;

import java.util.Optional;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/details/{dest-id}")
    public String showDestination(Model model, @PathVariable long id){
        Optional<Destination> destination = destinationService.findById(id);
        model.addAttribute("item-name", destination.get().getName());
        model.addAttribute("item-description", destination.get().getDescription());
        model.addAttribute("item-img", destination.get().getImageFile());

        for (int i = 0; i < Math.min(10, destination.get().getPlaces().size()); i++){
            model.addAttribute("name", destination.get().getPlaces().get(i).getName());
            model.addAttribute("type", "Place");
            //model.addAttribute("item-1-flag", destination.get().getFlag();
        }

        return "details";
    }

    @GetMapping("/details/{place-id}")
    public String showPlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("item-name", place.get().getName());
        model.addAttribute("item-description", place.get().getDescription());
        model.addAttribute("item-img", place.get().getImageFile());

        for (int i = 0; i < Math.min(10, place.get().getItineraries().size()); i++) {
            model.addAttribute("name", place.get().getItineraries().get(i).getName());
            model.addAttribute("type", "Itinerary");
            //model.addAttribute("item-1-flag", destination.get().getFlag();
        }

        return "details";
    }

    @GetMapping("/details/{itin-id}")
    public String showItinerary(Model model, @PathVariable long id){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        model.addAttribute("item-name", itinerary.get().getName());
        model.addAttribute("item-description", itinerary.get().getDescription());
        model.addAttribute("item-img", itinerary.get().getPlaces().get(0).getImageFile());

        for (int i = 0; i < Math.min(10, itinerary.get().getPlaces().size()); i++){
            model.addAttribute("name", itinerary.get().getPlaces().get(i).getName());
            model.addAttribute("type", "Place");
            //model.addAttribute("flag", destination.get().getFlag();
        }

        for (int i = 0; i < Math.min(10, itinerary.get().getReviews().size()); i++){
            model.addAttribute("user-img", itinerary.get().getReviews().get(i).getUser().getImageFile());
            //model.addAttribute("score", itinerary.get().getReviews().get(i).getPoints())
            model.addAttribute("review-name", itinerary.get().getReviews().get(i).getTitle());
            model.addAttribute("review-user", itinerary.get().getReviews().get(i).getUser());
            //model.addAttribute("date", itinerary.get().getReviews().get(i).getDate());
            model.addAttribute("review-comment", itinerary.get().getReviews().get(i).getDescription());
        }

        return "itinerary-details";
    }
}
