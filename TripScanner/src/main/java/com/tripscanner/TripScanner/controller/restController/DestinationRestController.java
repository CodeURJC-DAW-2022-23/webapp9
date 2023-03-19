package com.tripscanner.TripScanner.controller.restController;

import com.fasterxml.jackson.annotation.JsonView;
import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.rest.DestinationDetails;
import com.tripscanner.TripScanner.service.DestinationService;
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
@RequestMapping("/api/destinations")
public class DestinationRestController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @GetMapping("")
    public ResponseEntity<Page<Destination>> destinations(@RequestParam(defaultValue = "") String name,
                                                          @RequestParam(defaultValue = "id") String sort,
                                                          @RequestParam(defaultValue = "DESC") String order,
                                                          @RequestParam(defaultValue = "0") int page) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        return ResponseEntity.ok(destinationService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                PageRequest.of(page, 10, Sort.by(direction, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationDetails> destination(@PathVariable int id,
                                                          @RequestParam(defaultValue = "0") int placesPage) {
        Optional<Destination> optionalDestination = destinationService.findById(id);

        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            DestinationDetails destinationDetails = new DestinationDetails(destination,
                    placeService.findFromDestination(destination.getId(), PageRequest.of(placesPage, 10)));

            return ResponseEntity.ok(destinationDetails);
        }
        else return ResponseEntity.notFound().build();
    }

}
