package com.tripscanner.TripScanner.controller.restController;


import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
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

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UserService userService;


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
    public ResponseEntity<Destination> createDestination(@RequestBody Destination destination) {
        destinationService.save(destination);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(destination.getId()).toUri();
        return ResponseEntity.created(location).body(destination);
    }

    @PutMapping("/destinations/{id}")
    public ResponseEntity<Destination> editDestination(@PathVariable long id, @RequestBody Destination newDestination) {
        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent()) {
            newDestination.setId(id);
            destinationService.save(newDestination);
            return ResponseEntity.ok(destination.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/destinations/{id}")
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
    public ResponseEntity<Long> createNewItinerary(@RequestBody Itinerary itinerary, HttpServletRequest request) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        if (!itinerary.hasName()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Itinerary newItinerary;
        if (!itinerary.hasDescription())
            newItinerary = new Itinerary(itinerary.getName(), "", user, itinerary.isPublic());
        else newItinerary = new Itinerary(itinerary.getName(), itinerary.getDescription(), user, itinerary.isPublic());

        newItinerary.setImage(true);
        Resource image = new ClassPathResource("static/img/placeholder.jpg");
        newItinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        itineraryService.save(newItinerary);
        return new ResponseEntity<>(newItinerary.getId(), HttpStatus.CREATED);
    }


    @PutMapping("/itineraries/{id}")
    public ResponseEntity editItinerary(@PathVariable long id, @RequestBody Itinerary newItineraries, HttpServletRequest request) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            if (newItineraries.hasName()) itinerary.get().setName(newItineraries.getName());
            if (newItineraries.hasDescription()) itinerary.get().setDescription(newItineraries.getDescription());
            if (newItineraries.getPlaces() != null) {
                for (Place p : newItineraries.getPlaces()) {
                    List<Place> placeList = itinerary.get().getPlaces();
                    placeList.add(p);
                    itinerary.get().setPlaces(placeList);
                }
            }
            itinerary.get().setPublic(newItineraries.isPublic());
            newItineraries.setId(id);
            itineraryService.save(itinerary.get());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/itineraries/{id}")
    public ResponseEntity deleteItinerary(@PathVariable long id) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        if (itinerary.isPresent()) {
            itineraryService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
