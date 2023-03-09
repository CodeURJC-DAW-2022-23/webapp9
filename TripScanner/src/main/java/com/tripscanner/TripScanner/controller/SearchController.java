package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.*;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private SearchService searchService;

    private String searchResult;
    private Optional<Destination> destinationsAux;


    // Search methods for "view more details" link in main page
    @GetMapping("/search/destination")
    public String showSearchResultDestination(Model model, HttpSession session) {
        Page<Destination> destination = destinationService.findAll(PageRequest.of(0, 10));
        String name = "destination";
        session.setAttribute("searchResult", name);
        model.addAttribute("information", destination);
        return "search";
    }

    @GetMapping("/search/place")
    public String showSearchResultPlace(Model model, HttpSession session) {
        Pageable placePaged = PageRequest.of(0, 10);
        Page<Place> place = placeService.findAll(placePaged);
        String name = "place";
        session.setAttribute("searchResult", name);
        model.addAttribute("information", place);
        return "search";
    }

    @GetMapping("/search/itinerary")
    public String showSearchResultItinerary(Model model, HttpSession session) {
        Pageable itineraryPaged = PageRequest.of(0, 10);
        Page<Itinerary> itinerary = itineraryService.findAll(itineraryPaged);
        String name = "itinerary";
        session.setAttribute("searchResult", name);
        model.addAttribute("information", itinerary);
        return "search";
    }

    @GetMapping("/search/privateItinerary")
    public String showSearchResultPrivateItinerary(Model model, HttpSession session) {
        Pageable itineraryPaged = PageRequest.of(0, 10);
        String name = (String) session.getAttribute("searchResult");
        if (name == "itinerary") {
            List<Itinerary> itinerariesPrivate = itineraryService.findAll();
            for (Itinerary it : itinerariesPrivate) {
                if (it.isPublic() == false) {
                    model.addAttribute("information", it);
                }
            }
        } else {
            List<Itinerary> itinerariesPrivate = itineraryService.findByQuery(name, name, itineraryPaged);
            for (Itinerary it : itinerariesPrivate) {
                if (it.isPublic() == false) {
                    model.addAttribute("information", it);
                }
            }
        }

        return "search";
    }

    // Sort filter by country

   /* @GetMapping("/search/sortCountry")
    public String showSortedBycountry(Model model, @RequestParam("itemId" ) long itemId, HttpSession session){
        Page<Destination> dest = destinationService.findAll(PageRequest.of(0,10));
        model.addAttribute("items", dest);
        String name = (String) session.getAttribute("searchResult");
        destinationsAux = destinationService.findById(itemId);
        if(name == "destination"){
            List<Destination> destinationsCountry = destinationService.findByQueryCountry(destinationsAux);
            model.addAttribute("information", destinationsCountry);
        } else if(name == "place"){
            List<Place> placeCountry = placeService.findByQueryCountry(Long.parseLong(String.valueOf(destinationsAux)));
            model.addAttribute("information", placeCountry);
        } else if(name == "itinerary"){
            List<Itinerary> itinerariesCountry = itineraryService.findByQueryCountry(Long.parseLong(String.valueOf(destinationsAux)));
            model.addAttribute("information", itinerariesCountry);
        }
        return "search";
    }*/

    // Sort filter
    @GetMapping("/search/sort")
    public String showSearchSorted(Model model, @RequestParam("sortOption") String sortOption, HttpSession session) {
        String name = (String) session.getAttribute("searchResult");
        if (name == "destination") {
            if (sortOption.equals("popularity")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                Page<Destination> destinationsView = destinationService.findAll(pageable);
                model.addAttribute("information", destinationsView);
            } else if (sortOption.equals("asc")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                Page<Destination> destinationsView = destinationService.findAll(pageable);
                model.addAttribute("information", destinationsView);
            }

        } else if (name == "place") {
            if (sortOption.equals("popularity")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                Page<Place> placeView = placeService.findAll(pageable);
                model.addAttribute("information", placeView);
            } else if (sortOption.equals("asc")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                Page<Place> placeView = placeService.findAll(pageable);
                model.addAttribute("information", placeView);
            }

        } else if (name == "itinerary") {
            if (sortOption.equals("popularity")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                Page<Itinerary> itinerariesView = itineraryService.findAll(pageable);
                model.addAttribute("information", itinerariesView);
            } else if (sortOption.equals("asc")) {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                Page<Itinerary> itinerariesView = itineraryService.findAll(pageable);
                model.addAttribute("information", itinerariesView);
            }

        } else {
            List<Information> results = searchService.searchInfo(name, name);
            List<String> searchResult = results.stream().map(Information::getType).collect(Collectors.toList());
            for (String str : searchResult) {
                if (str.equals("Destination")) {
                    // sort for destinations
                    if (sortOption.equals("popularity")) {
                        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                        List<Destination> destinationsViews = destinationService.findByQuery(name, name, pageable);
                        model.addAttribute("information", destinationsViews);
                        return "search";
                    } else if (sortOption.equals("asc")) {
                        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                        List<Destination> destinationsAsc = destinationService.findByQuery(name, name, pageable);
                        model.addAttribute("information", destinationsAsc);
                        return "search";

                        // sort of places
                    }
                } else if (str.equals("Place")) {
                        if (sortOption.equals("popularity")) {
                            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                            List<Place> placeViews = placeService.findByQuery(name, name, pageable);
                            model.addAttribute("information", placeViews);
                            return "search";
                        } else if (sortOption.equals("asc")) {
                            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                            List<Place> placesAsc = placeService.findByQuery(name, name, pageable);
                            model.addAttribute("information", placesAsc);
                            return "search";
                        }

                        // sort of itineraries
                    } else if (str.equals("Itinerary")) {
                        if (sortOption.equals("popularity")) {
                            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "views"));
                            List<Itinerary> itinerariesViews = itineraryService.findByQuery(name, name, pageable);
                            model.addAttribute("information", itinerariesViews);
                            return "search";
                        } else if (sortOption.equals("asc")) {
                            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name")));
                            List<Itinerary> itinerariesAsc = itineraryService.findByQuery(name, name, pageable);
                            model.addAttribute("information", itinerariesAsc);
                            return "search";
                        }

                    }

                }
            }
        return "search";

        }




    // Filter by type of search
    @GetMapping("/search/filter")
    public String searchByType(@RequestParam(value = "option1", required = false) boolean option1,
                               @RequestParam(value = "option2", required = false) boolean option2,
                               @RequestParam(value = "option3", required = false) boolean option3,
                               HttpSession session,
                               Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        String name = (String) session.getAttribute("searchResult");
        if (option1) {
            List<Destination> destinations = destinationService.findByQuery(name, name, pageable);
            model.addAttribute("information", destinations);
            model.addAttribute("name", name);
            return "search";
        } else if (option2) {
            List<Place> places = placeService.findByQuery(name, name, pageable);
            model.addAttribute("information", places);
            return "search";
        } else if (option3) {
            List<Itinerary> itineraries = itineraryService.findByQuery(name, name, pageable);
            model.addAttribute("information", itineraries);
            return "search";
        } else {
            return "Not found";
        }

    }


    // Global search by word
    @GetMapping("/search")
    public String showSearchResult(@RequestParam("name") String name, Model model, HttpSession session) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Destination> destination = destinationService.findByQuery(name, name, pageable);
        List<Place> place = placeService.findByQuery(name, name, pageable);
        List<Itinerary> itinerary = itineraryService.findByQuery(name, name, pageable);
        session.setAttribute("searchResult", name);
        searchResult = name;
        if (!destination.isEmpty()) {
            model.addAttribute("information", destination);
            model.addAttribute("name", name);
            return "search";
        } else if (!place.isEmpty()) {
            model.addAttribute("information", place);
            model.addAttribute("name", name);
            return "search";
        } else if (!itinerary.isEmpty()) {
            model.addAttribute("information", itinerary);
            model.addAttribute("name", name);
            return "search";
        } else {
            return "Not found";
        }
    }
}




