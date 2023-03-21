package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.rest.PlaceDTO;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
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
@RequestMapping("/api/management/places")
public class RestPlaceManagementController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @GetMapping("")
    public ResponseEntity<Page<Place>> getPlaces(Pageable pageable){
        Page<Place> places = placeService.findAll(pageable);
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<Place> createPlace(@RequestBody PlaceDTO newPlace) {
        Place place = new Place(newPlace.getName(), newPlace.getDescription(), destinationService.findByName(newPlace.getDestination()).get());
        placeService.save(place);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(place.getId()).toUri();
        return ResponseEntity.created(location).body(place);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> editPlace(@PathVariable long id, @RequestBody PlaceDTO newPlaceDTO) throws SQLException {
        Optional<Place> place = placeService.findById(id);
        Place newPlace = new Place(newPlaceDTO.getName(), newPlaceDTO.getDescription(), destinationService.findByName(newPlaceDTO.getDestination()).get());
        if (place.isPresent()) {
            newPlace.setId(id);
            placeService.save(newPlace);
            return ResponseEntity.ok(place.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable long id) {
        Optional<Place> place = placeService.findById(id);
        if (place.isPresent()) {
            for (int i = 0; i < place.get().getItineraries().size(); i++) {
                place.get().getItineraries().get(i).getPlaces().remove(place.get());
                itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().remove(place);
                if (itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().isEmpty()) {
                    itineraryService.delete(place.get().getItineraries().get(i).getId());
                }
            }
            placeService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
