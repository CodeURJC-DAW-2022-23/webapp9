package com.tripscanner.TripScanner.controller;

import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.*;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

    @GetMapping("/details/dest-{id}")
    public String showDestination(Model model, @PathVariable long id){
        Optional<Destination> destination = destinationService.findById(id);
        model.addAttribute("item-name", destination.get().getName());
        model.addAttribute("item-description", destination.get().getDescription());
        model.addAttribute("item-img", destination.get().getImageFile());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < Math.min(10, destination.get().getPlaces().size()); i++){
            //places.add(new Information(destination.get().getFlagFile(), destination.get().getPlaces().get(i).getName(), "Place"));
        }
        model.addAttribute("information", places);

        return "details";
    }

    @GetMapping("/details/place-{id}")
    public String showPlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("item-name", place.get().getName());
        model.addAttribute("item-description", place.get().getDescription());
        model.addAttribute("item-img", place.get().getImageFile());

        List<Information> itineraries = new ArrayList<>();
        for (int i = 0; i < Math.min(10, place.get().getItineraries().size()); i++){
            //itineraries.add(new Information(place.get().getDestination().getFlagFile(), place.get().getItineraries().get(i).getName(), "Itinerary"));
        }
        model.addAttribute("information", itineraries);

        return "details";
    }

    @GetMapping("/details/itin-{id}")
    public String showItinerary(Model model, @PathVariable long id){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        model.addAttribute("item-name", itinerary.get().getName());
        model.addAttribute("item-description", itinerary.get().getDescription());
        model.addAttribute("item-img", itinerary.get().getPlaces().get(0).getImageFile());

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < Math.min(10, itinerary.get().getPlaces().size()); i++){
            //places.add(new Information(itinerary.get().getPlaces().get(i).getDestination().getFlagFile(), itinerary.get().getPlaces().get(i).getName(), "Place"));
        }
        model.addAttribute("information", places);

        List<Review> reviews = new ArrayList<>();
        for (int i = 0; i < Math.min(10, itinerary.get().getPlaces().size()); i++){
            reviews.add(itinerary.get().getReviews().get(i));
        }
        model.addAttribute("review", reviews);

        return "itinerary-details";
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

        return "details";
    }
}
