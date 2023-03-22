package com.tripscanner.TripScanner.controller.restController;


import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/destinations")
public class RestManagementDestinationController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private DestinationService destinationService;


    @GetMapping("")
    public ResponseEntity<Page<Destination>> getDestination(Pageable pageable) {
        Page<Destination> destinations = destinationService.findAll(pageable);
        if (!destinations.isEmpty()) {
            return ResponseEntity.ok(destinations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
        destinationService.save(destination);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(destination.getId()).toUri();
        return ResponseEntity.created(location).body(destination);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destination> editDestination(@PathVariable long id, @RequestBody Destination newDestination) throws SQLException {
        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent()) {
            if (newDestination.getImageFile() != null) {
                newDestination.setImageFile(BlobProxy.generateProxy(destination.get().getImageFile().getBinaryStream(), destination.get().getImageFile().length()));
            }
            newDestination.setId(id);
            destinationService.save(newDestination);
            return ResponseEntity.ok(destination.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Destination> deleteDestination(@PathVariable long id) {
        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent()) {
            for (int i = 0; i < destination.get().getPlaces().size(); i++) {
                Place place = destination.get().getPlaces().get(i);
                for (int j = 0; j < place.getItineraries().size(); j++) {
                    place.getItineraries().get(j).getPlaces().remove(place);
                    itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().remove(place);
                    if (itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().isEmpty()) {
                        itineraryService.delete(place.getItineraries().get(j).getId());
                    }
                }
            }
            destinationService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
