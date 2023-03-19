package com.tripscanner.TripScanner.controller.restController;


import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/")

public class RestManagementController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private DestinationService destinationService;


    //destination

    @GetMapping("/destinations/")
    public ResponseEntity<Destination> getDestination() {
        List<Destination> destinations = destinationService.findAll();
        if (!destinations.isEmpty()) {
            return ResponseEntity.ok(destinations.get(1));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/destinations/")
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination){
        destinationService.save(destination);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(destination.getId()).toUri();
        return ResponseEntity.created(location).body(destination);
    }



   //itinerary
   @GetMapping("/itineraries/")
    public ResponseEntity<Itinerary> getItinerary() {
        List<Itinerary> itinerary = itineraryService.findAll();
        if (!itinerary.isEmpty()) {
            return ResponseEntity.ok(itinerary.get(1));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/itineraries/")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Itinerary itinerary){
        itineraryService.save(itinerary);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(itinerary.getId()).toUri();
        return ResponseEntity.created(location).body(itinerary);
    }


    @PutMapping("/itineraries/{id}")
    public ResponseEntity<Itinerary> editItinerary(@PathVariable long id, @RequestBody Itinerary newItineraries){
        Itinerary itinerary = itineraryService.findById(id).orElseThrow();

        if(itinerary != null){
            newItineraries.setId(id);
            itineraryService.save(newItineraries);
            return ResponseEntity.ok(itinerary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/itineraries/{id}")
    public ResponseEntity<Itinerary> deleteItinerary(@PathVariable long id){
        Itinerary itinerary = itineraryService.findById(id).orElseThrow();
        if (itinerary != null){
            itineraryService.delete(id);
            return ResponseEntity.ok(itinerary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
