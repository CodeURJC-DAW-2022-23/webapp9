package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDTO;
import com.tripscanner.TripScanner.model.rest.ItineraryDetails;
import com.tripscanner.TripScanner.model.rest.PlaceDetails;
import com.tripscanner.TripScanner.model.rest.PlaceIdDTO;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import com.tripscanner.TripScanner.service.ReviewService;
import com.tripscanner.TripScanner.utils.PdfGenerator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryRestController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Searches for Itineraries in the database. Filters can be applied optionally.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Itinerary.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            )
    })
    @GetMapping("")
    public ResponseEntity<Page<Itinerary>> getItineraries(HttpServletRequest request,
                                                          @Parameter(description="search query") @RequestParam(defaultValue = "") String name,
                                                          @Parameter(description="sorting type: id, name, views") @RequestParam(defaultValue = "id") String sort,
                                                          @Parameter(description="order specifier") @RequestParam(defaultValue = "DESC") String order,
                                                          @Parameter(description="page number") @RequestParam(defaultValue = "0") int page) {
        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        Principal principalUser = request.getUserPrincipal();
        Page<Itinerary> itineraries;

        if (principalUser == null) {
            itineraries = itineraryService.findAllByNameOrDescriptionLikeIgnoreCasePublic(name, name, PageRequest.of(page, 10));
        } else {
            User user = userService.findByUsername(principalUser.getName()).get();
            itineraries = itineraryService.findAllByNameOrDescriptionAndUserOrPublicLikeIgnoreCase(name, name, user.getUsername(),
                    PageRequest.of(page, 10, Sort.by(direction, sort)));
        }

        return new ResponseEntity<>(itineraries, HttpStatus.OK);
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

    @PostMapping("")
    public ResponseEntity<Itinerary> createNewItinerary(@RequestBody ItineraryDTO itineraryDTO, HttpServletRequest request) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(principalUser.getName()).get();
        if (itineraryDTO.getName() == null || itineraryDTO.getDescription() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Itinerary newItinerary = new Itinerary(itineraryDTO, user, user.getRoles().contains("ADMIN"));
        newItinerary.setImage(true);
        Resource image = new ClassPathResource("/static/img/placeholder.jpeg");
        newItinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        itineraryService.save(newItinerary);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newItinerary.getId()).toUri();

        return ResponseEntity.created(location).body(newItinerary);
    }

    // Note that "/api/itineraries/{id}/copy" follows naming conventions for REST APIs, as verbs CAN be used inside subrecurses,
    // and only nouns are used for resources. Same goes for "/api/itineraries/{id}/export". Also, POST method is allowed here,
    // as new data is added to the database, even though the body of the request is not needed.
    @PostMapping("/{id}/copy")
    public ResponseEntity<Itinerary> copyItinerary(@PathVariable long id, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary copy = optionalItinerary.get().copy(user);
        List<Itinerary> userItineraries = user.getItineraries();
        userItineraries.add(copy);
        user.setItineraries(userItineraries);

        itineraryService.save(copy);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(copy.getId()).toUri();

        return ResponseEntity.created(location).body(copy);
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

    @DeleteMapping("/{itineraryId}/places/{placeId}")
    public ResponseEntity<Page<Place>> deletePlacesFromItinerary(@PathVariable long itineraryId, @PathVariable long placeId, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(itineraryId);
        Optional<Place> optionalPlace = placeService.findById(placeId);
        if (principalUser == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (!optionalPlace.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();
        Place place = optionalPlace.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername()) && !user.getRoles().contains("ADMIN"))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        List<Place> itineraryPlaces = itinerary.getPlaces();
        itineraryPlaces.remove(place);
        itinerary.setPlaces(itineraryPlaces);
        itineraryService.save(itinerary);

        return ResponseEntity.ok().body(placeService.findFromItinerary(itineraryId, PageRequest.of(0, 10)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Itinerary> editItineraryById(@RequestBody ItineraryDTO itineraryDTO, @PathVariable long id, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (optionalItinerary.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (itineraryDTO.getName() != null) itinerary.setName(itineraryDTO.getName());
        if (itineraryDTO.getDescription() != null) itinerary.setDescription(itineraryDTO.getDescription());
        itinerary.setPublic(itineraryDTO.isPublic());

        itineraryService.save(itinerary);

        return ResponseEntity.ok().body(itinerary);
    }

    @PostMapping("/{itineraryId}/places")
    public ResponseEntity<Page<Place>> editPlaces(@PathVariable long itineraryId, @RequestBody PlaceIdDTO placeIdDTO, HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(itineraryId);
        Optional<Place> optionalPlace = placeService.findById(placeIdDTO.getPlaceId());
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (!optionalPlace.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();
        Place place = optionalPlace.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        List<Place> itineraryPlaces = itinerary.getPlaces();
        itineraryPlaces.add(place);
        itinerary.setPlaces(itineraryPlaces);
        itineraryService.save(itinerary);

        return ResponseEntity.ok().body(placeService.findFromItinerary(itinerary.getId(), PageRequest.of(0, 10)));
    }

    @PutMapping(value = "/{id}/image", consumes = {"multipart/form-data", "image/jpeg", "image/png"})
    public ResponseEntity<Resource> editItineraryImage(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable long id, HttpServletRequest request) throws IOException, SQLException {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        itinerary.setImage(true);
        itinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        itineraryService.save(itinerary);

        Resource file = new InputStreamResource(imageFile.getInputStream());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(itinerary.getImageFile().length()).body(file);
    }

    @Operation(summary = "Get Detailed information about a specific Itinerary.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Itineraries.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItineraryDetails.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid permissions to request places from a private Itinerary.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Itinerary.",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItineraryDetails> itinerary(HttpServletRequest request,
                                                      @Parameter(description="itinerary id") @PathVariable int id,
                                                      @Parameter(description="places page number") @RequestParam(defaultValue = "0") int placesPage,
                                                      @Parameter(description="reviews page number") @RequestParam(defaultValue = "0") int reviewsPage) {

        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();

            if (usr == null || !itinerary.getUser().getUsername().equals(usr.getName()))
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ItineraryDetails itineraryDetails = new ItineraryDetails(itinerary,
                placeService.findFromItinerary(itinerary.getId(), PageRequest.of(placesPage, 10)),
                reviewService.findFromItinerary(itinerary.getId(), PageRequest.of(reviewsPage, 10)));

        return ResponseEntity.ok(itineraryDetails);
    }

    @Operation(summary = "Returns the image of the desired Itinerary.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucessfully returned the Itinerary's image.",
                    content = {@Content(
                            mediaType = "image/jpeg"
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Itinerary's image.",
                    content = @Content
            )
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> downloadImage(@Parameter(description="itinerary id") @PathVariable long id) throws SQLException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent() && itinerary.get().getImageFile() != null) {
            Resource file = new InputStreamResource(itinerary.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(itinerary.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get Detailed information about a specific Itinerary.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Places frmo an Itinerary.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Invalid permissions to request places from a private Itinerary.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Itinerary.",
                    content = @Content
            )
    })
    @GetMapping("/{id}/places")
    public ResponseEntity<Page<Place>> getPlacesInUserItinerary(HttpServletRequest request,
                                                                @PathVariable long id,
                                                                @RequestParam(defaultValue = "0") int page) {
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();
            if (usr == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            if (!itinerary.getUser().getUsername().equals(usr.getName())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(placeService.findFromItinerary(id, PageRequest.of(page, 10)), HttpStatus.OK);
    }

}
