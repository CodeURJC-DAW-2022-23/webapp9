package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.tripscanner.TripScanner.utils.PdfGenerator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/itineraries")
public class RestItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Page<Itinerary>> getItineraries(HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Page<Itinerary> itineraries;
        if (principalUser == null) {
            itineraries = itineraryService.findAllPublic(PageRequest.of(0, 5));
        } else {
            User user = userService.findByUsername(principalUser.getName()).get();
            itineraries = itineraryService.findAllByUserOrPublic(user.getUsername(), PageRequest.of(0, 5));
        }

        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Itinerary> getItinerary(HttpServletRequest request, @PathVariable long id) {
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();
            if (usr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (!itinerary.getUser().getUsername().equals(usr.getName())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(itinerary, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getItineraryImage(@PathVariable long id, HttpServletRequest request) throws SQLException {
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();
            if (usr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (!itinerary.getUser().getUsername().equals(usr.getName())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Resource img = new InputStreamResource(itinerary.getImageFile().getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(itinerary.getImageFile().length()).body(img);
    }

    @GetMapping("/{id}/places")
    public ResponseEntity<Page<Place>> getPlacesInUserItinerary(HttpServletRequest request, @PathVariable long id) {
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();
            if (usr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (!itinerary.getUser().getUsername().equals(usr.getName())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Page<Place> places = placeService.findAllByItineraryId(id, PageRequest.of(0, 5));

        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @GetMapping("/{id}/export")
    public ResponseEntity getPdfFromItinerary(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Itinerary itinerary = optionalItinerary.get();
        PdfGenerator generator = new PdfGenerator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        generator.generate(itinerary, response);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=itinerary-" + id + ".pdf");
        response.setContentLength(baos.size());

        ServletOutputStream outputStream = response.getOutputStream();
        baos.writeTo(outputStream);
        outputStream.flush();

        return ResponseEntity.ok().body(baos.toByteArray());
    }

    @PostMapping("/")
    public ResponseEntity<Long> createNewItinerary(@RequestBody Itinerary itinerary, HttpServletRequest request) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        if (!itinerary.hasName()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Itinerary newItinerary;
        if (!itinerary.hasDescription()) newItinerary = new Itinerary(itinerary.getName(), "", user, itinerary.isPublic());
        else newItinerary = new Itinerary(itinerary.getName(), itinerary.getDescription(), user, itinerary.isPublic());

        newItinerary.setImage(true);
        Resource image = new ClassPathResource("/static/img/placeholder.jpeg");
        newItinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        itineraryService.save(newItinerary);
        return new ResponseEntity<>(newItinerary.getId(), HttpStatus.CREATED);
    }

    // Note that "/api/itineraries/{id}/copy" follows naming conventions for REST APIs, as verbs CAN be used inside subrecurses,
    // and only nouns are used for resources. Same goes for "/api/itineraries/{id}/export". Also, POST method is allowed here,
    // as new data is added to the database, even though the body of the request is not needed.
    @PostMapping("/{id}/copy")
    public ResponseEntity<Itinerary> copyItinerary(@PathVariable long id, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary copy = optionalItinerary.get().copy(user);
        List<Itinerary> userItineraries = user.getItineraries();
        userItineraries.add(copy);
        user.setItineraries(userItineraries);

        itineraryService.save(copy);
        return new ResponseEntity<>(copy, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItinerary(@PathVariable long id, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity(HttpStatus.FORBIDDEN);
        itineraryService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity editItineraryById(@RequestBody Itinerary newData, @PathVariable long id, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity(HttpStatus.FORBIDDEN);
        if (newData.hasName()) itinerary.setName(newData.getName());
        if (newData.hasDescription()) itinerary.setDescription(newData.getDescription());
        if (newData.getPlaces() != null) {
            for (Place p : newData.getPlaces()) {
                List<Place> placeList = itinerary.getPlaces();
                placeList.add(p);
                itinerary.setPlaces(placeList);
            }
        }
        itinerary.setPublic(newData.isPublic());

        itineraryService.save(itinerary);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/image", consumes = {"multipart/form-data", "image/jpeg", "image/png"})
    public ResponseEntity editItineraryImage(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable long id, HttpServletRequest request) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity(HttpStatus.FORBIDDEN);

        itinerary.setImage(true);
        itinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        itineraryService.save(itinerary);
        return new ResponseEntity(HttpStatus.OK);
    }


}
