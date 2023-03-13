package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class SearchController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/search")
    public String showSearchResult(@RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "itinerary") String type,
                                   @RequestParam(defaultValue = "id") String sort,
                                   @RequestParam(defaultValue = "DESC") String order,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {

        model.addAttribute("hideSearch", true);
        model.addAttribute("name", name);

        model.addAttribute("isItinerary", type.equals("itinerary"));
        model.addAttribute("isPlace", type.equals("place"));
        model.addAttribute("isDestination", type.equals("destination"));

        model.addAttribute("isId", sort.equals("id"));
        model.addAttribute("isViews", sort.equals("views"));
        model.addAttribute("isName", sort.equals("name"));

        model.addAttribute("isDesc", order.equals("DESC"));
        model.addAttribute("isAsc", order.equals("ASC"));

        return "search";
    }

    @GetMapping("/results")
    public String results(@RequestParam(defaultValue = "") String name,
                          @RequestParam(defaultValue = "itinerary") String type,
                          @RequestParam(defaultValue = "id") String sort,
                          @RequestParam(defaultValue = "DESC") String order,
                          @RequestParam int page,
                          Model model) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else direction = Sort.Direction.ASC;

        name = name.toLowerCase();

        switch (type) {
            case "itinerary":
                model.addAttribute("information",
                        itineraryService.findAllByNameOrDescriptionLike(name, name,
                                PageRequest.of(page, 10, Sort.by(direction, sort))));
                return "searchResult";
            case "destination":
                model.addAttribute("information",
                        destinationService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                                PageRequest.of(page, 10, Sort.by(direction, sort))));
                return "searchResult";
            case "place":
                model.addAttribute("information",
                        placeService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                                PageRequest.of(page, 10, Sort.by(direction, sort))));
                return "searchResult";
        }

        return "searchResult";
    }

}




