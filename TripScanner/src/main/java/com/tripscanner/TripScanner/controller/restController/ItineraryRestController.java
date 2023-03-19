package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryRestController {

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("")
    public ResponseEntity<Page<Itinerary>> itineraries(@RequestParam(defaultValue = "") String name,
                                                       @RequestParam(defaultValue = "id") String sort,
                                                       @RequestParam(defaultValue = "DESC") String order,
                                                       @RequestParam(defaultValue = "0") int page) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        return ResponseEntity.ok(itineraryService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                PageRequest.of(page, 10, Sort.by(direction, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Itinerary> itinerary(@PathVariable int id) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) return ResponseEntity.ok(itinerary.get());
        else return ResponseEntity.notFound().build();
    }

}
