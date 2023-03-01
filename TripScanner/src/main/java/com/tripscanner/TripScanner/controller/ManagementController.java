package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.*;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/management/destination")
    public String showManagementDest(Model model, Pageable pageable){
        model.addAttribute("hasFlag", true);
        model.addAttribute("userItems", false);
        Page<Destination> dest = destinationService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", dest);
        model.addAttribute("type", "Destination");
        return "management";
    }
    @GetMapping("/management/user")
    public String showManagementUser(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("items", false);
        Page<User> users = userService.findAll(PageRequest.of(0,10));
        model.addAttribute("userItems", users);
        model.addAttribute("type", "User");
        return "management";
    }
    @GetMapping("/management/itinerary")
    public String showManagementItin(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("userItems", false);
        Page<Itinerary> itin = itineraryService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", itin);
        model.addAttribute("type", "Itinerary");
        return "management";
    }
    @GetMapping("/management/place")
    public String showManagementPlace(Model model, Pageable pageable){
        model.addAttribute("hasFlag", false);
        model.addAttribute("userItems", false);
        Page<Place> places = placeService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", places);
        model.addAttribute("type", "Place");
        return "management";
    }

    @GetMapping("/management/place/delete/{id}")
    public String deletePlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        for(int i = 0; i < place.get().getItineraries().size(); i++){
            place.get().getItineraries().get(i).getPlaces().remove(place.get());
            itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().remove(place);
            if (itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().isEmpty()){
                itineraryService.delete(place.get().getItineraries().get(i).getId());
            }
        }
        placeService.delete(id);
        return "redirect:/management/place/";
    }

    @GetMapping("/management/itinerary/delete/{id}")
    public String deleteItinerary(Model model, @PathVariable long id){
        itineraryService.delete(id);
        return "redirect:/management/itinerary/";
    }

    @GetMapping("/management/destination/delete/{id}")
    public String deleteDestination(Model model, @PathVariable long id){
        Optional<Destination> dest = destinationService.findById(id);
        for(int i = 0; i < dest.get().getPlaces().size(); i++){
            Place place = dest.get().getPlaces().get(i);
            for(int j = 0; j < place.getItineraries().size(); j++){
                place.getItineraries().get(j).getPlaces().remove(place);
                itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().remove(place);
                if (itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().isEmpty()){
                    itineraryService.delete(place.getItineraries().get(j).getId());
                }
            }
        }
        destinationService.delete(id);
        return "redirect:/management/destination/";
    }

    @GetMapping("/management/user/delete/{id}")
    public String deleteUser(Model model, @PathVariable long id){
        userService.delete(id);
        return "redirect:/management/user/";
    }
}
