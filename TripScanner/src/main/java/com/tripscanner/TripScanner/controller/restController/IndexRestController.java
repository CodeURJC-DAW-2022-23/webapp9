package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.rest.IndexDetails;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/index")
public class IndexRestController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("")
    public ResponseEntity<IndexDetails> itineraries() {

        // Top items
        Destination topDestination = destinationService.findAll(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "views"))).getContent().get(0);

        Place topPlace = placeService.findAll(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "views"))).getContent().get(0);

        Itinerary topItinerary = itineraryService.findAllPublic(
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "views"))).getContent().get(0);


        // Top 5 destinations
        List<Destination> topDestinations = destinationService.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views"))).toList();


        // Newest 3 items
        List<Destination> newestDestinations = destinationService.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))).toList();

        List<Place> newestPlaces = placeService.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))).toList();

        List<Itinerary> newestItineraries = itineraryService.findAllPublic(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))).toList();

        return ResponseEntity.ok(new IndexDetails(topDestination, topItinerary, topPlace, topDestinations,
                    newestDestinations, newestItineraries, newestPlaces));
    }

}
