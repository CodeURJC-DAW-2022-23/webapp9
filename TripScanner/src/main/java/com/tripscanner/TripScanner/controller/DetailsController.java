package com.tripscanner.TripScanner.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.*;
import com.tripscanner.TripScanner.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DetailsController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

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

        destination.get().setViews(destination.get().getViews() + 1);
        destinationService.save(destination.get());

        return "details";
    }

    @GetMapping("/details/destination/{id}/save")
    public String saveItinerary(HttpServletRequest request, @PathVariable long id) {
        Itinerary currItinerary = itineraryService.findById(id).get();
        User currUser = userService.findByUsername(request.getUserPrincipal().getName()).get();
        List<Itinerary> userItineraries = currUser.getItineraries();
        userItineraries.add(currItinerary.copy(currUser));
        currUser.setItineraries(userItineraries);
        userService.save(currUser);

        return "redirect:/myItineraries";
    }

    @GetMapping("/details/destination/{id}/delete")
    public String deleteItinerary(HttpServletRequest request, @PathVariable long id) {
        User currUser = userService.findByUsername(request.getUserPrincipal().getName()).get();
        currUser.getItineraries().remove(itineraryService.findById(id).get());
        itineraryService.delete(id);

        return "redirect:/myItineraries";
    }

    @GetMapping("/details/place/{id}")
    public String showPlace(Model model, HttpServletRequest request, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("item", place.get());

        List<Information> itineraries = new ArrayList<>();
        for (int i = 0; i < Math.min(3, place.get().getItineraries().size()); i++){
            itineraries.add(place.get().getItineraries().get(i));
        }
        model.addAttribute("information", itineraries);
        model.addAttribute("hide", true);
        model.addAttribute("isPlace", true);
        model.addAttribute("isLogged", request.getUserPrincipal() != null);

        if (request.getUserPrincipal() != null) {
            List<Itinerary> ownedItineraries = userService.findByUsername(request.getUserPrincipal().getName()).get().getItineraries();
            System.out.println(ownedItineraries);
            if (ownedItineraries.isEmpty()) model.addAttribute("ownedItineraries", false);
            else model.addAttribute("ownedItineraries", ownedItineraries);
        }

        place.get().setViews(place.get().getViews() + 1);
        placeService.save(place.get());

        return "details";
    }

    @GetMapping("/details/itinerary/{id}")
    public String showItinerary(Model model, HttpServletRequest request, @PathVariable long id, Pageable pageable){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        Principal currUser = request.getUserPrincipal();
        model.addAttribute("item", itinerary.get());
        model.addAttribute("isItinerary", true);

        if (itinerary.get().getUser() == userService.findByUsername(currUser.getName()).get()) {
            model.addAttribute("isOwned", true);
        } else {
            model.addAttribute("isOwned", false);
        }

        List<Information> places = new ArrayList<>();
        for (int i = 0; i < Math.min(3, itinerary.get().getPlaces().size()); i++){
            places.add(itinerary.get().getPlaces().get(i));
        }
        model.addAttribute("information", places);

        model.addAttribute("hide", false);

        Page<Review> reviews = reviewService.getItinReviews(itinerary.get(), PageRequest.of(0, 10));
        model.addAttribute("review", reviews);
        model.addAttribute("isLogged", request.getUserPrincipal() != null);

        return "details";
    }

}
