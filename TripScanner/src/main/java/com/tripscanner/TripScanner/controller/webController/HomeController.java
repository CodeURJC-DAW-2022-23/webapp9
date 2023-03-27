package com.tripscanner.TripScanner.controller.webController;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView showHomePage(Model model, @RequestParam Map<String, Integer> chartData, HttpServletRequest request) {

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
        Principal principalUser = request.getUserPrincipal();
        Page<Itinerary> itinerary;
        if (principalUser != null) {
            User user = userService.findByUsername(principalUser.getName()).get();
            itinerary = itineraryService.findAllByUserOrPublic(user.getUsername(), itineraryPaged);
        } else itinerary = itineraryService.findAllPublic(itineraryPaged);
        model.addAttribute("itinerary", itinerary);

        // Create graph

        List<Destination> dests = sortListByViews(destinationService.findAll());
        List<Long> views = new ArrayList<>();

        for (Destination d : dests) {
            views.add(d.getViews());
        }
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("places", dests);
        mav.addObject("views", views);
        return mav;
    }

    private List<Destination> sortListByViews(List<Destination> destList) {
        Page<Destination> popularDestination = destinationService.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views")));
        return popularDestination.toList();
    }
}
