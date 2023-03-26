package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDTO;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/itineraries")
public class ManagementItineraryRestController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<Itinerary>> getItinerary(Pageable pageable) {
        Page<Itinerary> itinerary = itineraryService.findAll(pageable);
        if (!itinerary.isEmpty()) {
            return ResponseEntity.ok(itinerary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("")
    public ResponseEntity<Itinerary> createNewItinerary(@RequestBody ItineraryDTO itinerary) throws IOException {

        Optional<User> user = userService.findByUsername(itinerary.getUser());
        if (user.isEmpty() || itinerary.getName() == null)
            return ResponseEntity.badRequest().build();

        if (itinerary.getDescription() == null)
            itinerary.setDescription("");

        Itinerary newItinerary = new Itinerary(itinerary, user.get());
        newItinerary.setPublic(user.get().getRoles().contains("ADMIN"));

        newItinerary.setImage(true);
        Resource image = new ClassPathResource("static/img/placeholder.jpg");
        newItinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        itineraryService.save(newItinerary);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newItinerary.getId()).toUri();
        return ResponseEntity.created(location).body(newItinerary);
    }



    @PutMapping("/{id}")
    public ResponseEntity editItinerary(@PathVariable long id, @RequestBody ItineraryDTO newItineraries) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            if (newItineraries.hasName()) itinerary.get().setName(newItineraries.getName());
            if (newItineraries.hasDescription()) itinerary.get().setDescription(newItineraries.getDescription());
            if (newItineraries.getPlace() != null) {
                for (Place p : newItineraries.getPlace()) {
                    List<Place> placeList = itinerary.get().getPlaces();
                    placeList.add(p);
                    itinerary.get().setPlaces(placeList);
                }
            }
            if (newItineraries.getUser() != null) {
                itinerary.get().setUser(userService.findByUsername(newItineraries.getUser()).get());
            }
            itinerary.get().setPublic(newItineraries.isPublic());
            itinerary.get().setId(id);
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

    @PostMapping("/{id}/image")
    public ResponseEntity<Itinerary> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            Itinerary newitinerary = itinerary.get();

            newitinerary.setImage(true);
            newitinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));

            itineraryService.save(newitinerary);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/itineraries/" + id + "/image");
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Itinerary> editImage(@PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            Itinerary newitinerary = itinerary.get();
            newitinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            itineraryService.save(newitinerary);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/itineraries/" + id + "/image");
            return ResponseEntity.created(location).body(newitinerary);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
