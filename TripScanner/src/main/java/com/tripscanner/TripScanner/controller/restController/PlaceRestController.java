package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.rest.DestinationDetails;
import com.tripscanner.TripScanner.model.rest.PlaceDetails;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlaceRestController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("")
    public ResponseEntity<Page<Place>> places(@RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "id") String sort,
                                              @RequestParam(defaultValue = "DESC") String order,
                                              @RequestParam(defaultValue = "0") int page) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        return ResponseEntity.ok(placeService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                PageRequest.of(page, 10, Sort.by(direction, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDetails> destination(@PathVariable int id,
                                                    @RequestParam(defaultValue = "0") int placesPage) {
        Optional<Place> optionalPlace = placeService.findById(id);

        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            PlaceDetails placeDetails = new PlaceDetails(place,
                    itineraryService.findFromPlace(place.getId(), PageRequest.of(placesPage, 10)));

            return ResponseEntity.ok(placeDetails);
        }
        else return ResponseEntity.notFound().build();
    }

}
