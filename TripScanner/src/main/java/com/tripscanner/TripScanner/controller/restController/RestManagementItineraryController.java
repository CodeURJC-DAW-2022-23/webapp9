package com.tripscanner.TripScanner.controller.restController;


import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/management/itinerary")
public class RestManagementItineraryController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @GetMapping(" ")
    public ResponseEntity<Page<Itinerary>>getItinerary(Pageable pageable) {
        Page<Itinerary> itinerary = itineraryService.findAll(pageable);
        if (!itinerary.isEmpty()) {
            return ResponseEntity.ok(itinerary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(" ")
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


    @PutMapping("/{id}")
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

    @DeleteMapping("/{id}")
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
