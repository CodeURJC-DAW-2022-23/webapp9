package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDTO;
import com.tripscanner.TripScanner.model.rest.ItineraryDetails;
import com.tripscanner.TripScanner.model.rest.PlaceDetails;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
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

    @GetMapping("")
    public ResponseEntity<Page<Itinerary>> getItineraries(HttpServletRequest request,
                                                          @RequestParam(defaultValue = "") String name,
                                                          @RequestParam(defaultValue = "id") String sort,
                                                          @RequestParam(defaultValue = "DESC") String order,
                                                          @RequestParam(defaultValue = "0") int page) {
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

    @Operation(summary = "Generate a pdf for an itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Itinerary successfully exported to pdf",
                    content = {@Content(
                            mediaType = "application/pdf"
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Operation not supported for users without an account",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary with specified id not found",
                    content = @Content
            )
    })

    @GetMapping("/{id}/pdf")
    public ResponseEntity getPdfFromItinerary(
            @Parameter(description = "id of the itinerary to be exported")
            @PathVariable long id, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

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

    @Operation(summary = "Creates a new itinerary if itiDTO is present. If it is not present and there is a copyFrom id, copies the itinerary with id = copyFrom")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Newly created itinerary",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "itiDTO data missing for POSTing new itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Operation not supported for users without an account",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary to copy from not found",
                    content = @Content
            )
    })

    @PostMapping("")
    public ResponseEntity<Itinerary> createOrCopyItinerary(
            @Parameter(description = "id of the itinerary to copy")
            @RequestParam(required = false) Optional<Long> copyFrom,
            @Parameter(description = "data of the itinerary to create")
            @RequestBody(required = false) Optional<ItineraryDTO> itiDTO,
            HttpServletRequest request) throws IOException {
        if (copyFrom.isPresent()) {
            Principal principalUser = request.getUserPrincipal();
            Optional<Itinerary> optionalItinerary = itineraryService.findById(copyFrom.get());
            if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
            if (principalUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

            User user = userService.findByUsername(principalUser.getName()).get();
            Itinerary copy = optionalItinerary.get().copy(user);
            List<Itinerary> userItineraries = user.getItineraries();
            userItineraries.add(copy);
            user.setItineraries(userItineraries);

            itineraryService.save(copy);

            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(copy.getId()).toUri();

            return ResponseEntity.created(location).body(copy);
        }

        else if (!itiDTO.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        ItineraryDTO itineraryDTO = itiDTO.get();

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

    @Operation(summary = "Delete an itinerary given its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully deleted the itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Operation not supported for not registered users",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItinerary(
            @Parameter(description = "id of the itinerary to be deleted")
            @PathVariable long id,
            HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity(HttpStatus.FORBIDDEN);
        itineraryService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Remove a place from an owned itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place removed successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Login needed to perform operation or User is not owner of the itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary or place not found",
                    content = @Content
            )
    })

    @DeleteMapping("/{itineraryId}/places/{placeId}")
    public ResponseEntity<Page<Place>> deletePlacesFromItinerary(
            @Parameter(description = "id of the itinerary to modify")
            @PathVariable long itineraryId,
            @Parameter(description = "id of the place to remove")
            @PathVariable long placeId,
            HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(itineraryId);
        Optional<Place> optionalPlace = placeService.findById(placeId);
        if (principalUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);
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

    @Operation(summary = "Edit an itinerary given its id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Itinerary edited successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Login needed to perform operation or User is not owner of the itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}")
    public ResponseEntity<Itinerary> editItineraryById(
            @Parameter(description = "new data of the itinerary")
            @RequestBody ItineraryDTO itineraryDTO,
            @Parameter(description = "id of the itinerary to edit")
            @PathVariable long id,
            HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (optionalItinerary.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (itineraryDTO.getName() != null) itinerary.setName(itineraryDTO.getName());
        if (itineraryDTO.getDescription() != null) itinerary.setDescription(itineraryDTO.getDescription());
        itinerary.setPublic(itineraryDTO.isPublic());

        itineraryService.save(itinerary);

        return ResponseEntity.ok().body(itinerary);
    }

    @Operation(summary = "Add a place to an owned itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place added successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Login needed to perform operation or User is not owner of the itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary or place not found",
                    content = @Content
            )
    })

    @PutMapping("/{itineraryId}/places")
    public ResponseEntity<Page<Place>> editPlaces(
            @Parameter(description = "id of the itinerary you want to add a place to")
            @PathVariable long itineraryId,
            @Parameter(description = "id of the place you want to add to the itinerary")
            @RequestBody long placeId,
            HttpServletRequest request) {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(itineraryId);
        Optional<Place> optionalPlace = placeService.findById(placeId);
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

    @Operation(summary = "Edit the image of an itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image edited successfully",
                    content = {@Content(
                            mediaType = "image/jpeg"
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Login needed to perform operation or User is not owner of the itinerary",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            )
    })

    @PutMapping(value = "/{id}/image", consumes = {"multipart/form-data", "image/jpeg", "image/png"})
    public ResponseEntity<Resource> editItineraryImage(
            @Parameter(description = "image to set as itinerary image")
            @RequestParam("imageFile") MultipartFile imageFile,
            @Parameter(description = "id of the itinerary you want to add an image to")
            @PathVariable long id,
            HttpServletRequest request) throws IOException, SQLException, URISyntaxException {
        Principal principalUser = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(principalUser.getName()).get();
        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.getUser().getUsername().equals(user.getUsername())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        itinerary.setImage(true);
        itinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        itineraryService.save(itinerary);

        Resource file = new InputStreamResource(imageFile.getInputStream());

        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
        URI location = new URI(baseUrl + "/api/itineraries/" + id + "/image");

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg", HttpHeaders.CONTENT_LOCATION, location.toString())
                .contentLength(itinerary.getImageFile().length()).body(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItineraryDetails> itinerary(HttpServletRequest request,
                                                      @PathVariable int id,
                                                      @RequestParam(defaultValue = "0") int placesPage,
                                                      @RequestParam(defaultValue = "0") int reviewsPage) {

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

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent() && itinerary.get().getImageFile() != null) {
            Resource file = new InputStreamResource(itinerary.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(itinerary.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/places")
    public ResponseEntity<Page<Place>> getPlacesInUserItinerary(HttpServletRequest request,
                                                                @PathVariable long id,
                                                                @RequestParam(defaultValue = "0") int page) {
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            Principal usr = request.getUserPrincipal();
            if (usr == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (!itinerary.getUser().getUsername().equals(usr.getName())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(placeService.findFromItinerary(id, PageRequest.of(page, 10)), HttpStatus.OK);
    }

}
