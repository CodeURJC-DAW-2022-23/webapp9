package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.Review;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDTO;
import com.tripscanner.TripScanner.model.rest.ItineraryDetailsDTO;
import com.tripscanner.TripScanner.model.rest.ReviewDTO;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ReviewService;
import com.tripscanner.TripScanner.service.UserService;
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

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=itinerary-" + id + ".pdf");
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        PdfGenerator generator = new PdfGenerator();
        generator.generate(itinerary.get(), response);

        return ResponseEntity.ok(response);
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
        newItinerary.setPublic(itiDTO.get().isPublic());
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
        for (Review r : itinerary.getReviews()) {
            reviewService.delete(r.getId());
        }

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

    @PostMapping("/{itineraryId}/places")
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

    @Operation(summary = "Add a review to an itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Review created successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Review's score is not between 0 and 5",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Operation not allowed for unregistered users or the itinerary is private",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Needed fields for review not found",
                    content = @Content
            )
    })

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDTO> addReview(HttpServletRequest request,
                                               @Parameter(description = "id of the itinerary to add a review to")
                                               @PathVariable long id,
                                               @Parameter(description = "body of the review object to be added to the itinerary")
                                               @RequestBody ReviewDTO review) {
        Principal userPrincipal = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (review.getTitle().isEmpty() || review.getTitle().isBlank() || review.getDescription().isEmpty() || review.getDescription().isBlank()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (review.getScore() < 0 || review.getScore() > 5) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.findByUsername(userPrincipal.getName()).get();
        Itinerary itinerary = optionalItinerary.get();

        if (!itinerary.isPublic()) {
            if (!itinerary.getUser().getUsername().equals(user.getUsername()) && !user.getRoles().contains("ADMIN")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Review> reviews = itinerary.getReviews();
        Review newReview = new Review(review.getTitle(), review.getDescription(), review.getScore());
        newReview.setUser(user);
        newReview.setItinerary(itinerary);
        reviewService.save(newReview);
        reviews.add(newReview);
        itineraryService.save(itinerary);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newReview.getId()).toUri();

        return ResponseEntity.created(location).body(new ReviewDTO(newReview));
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

    @Operation(summary = "Get Detailed information about a specific Itinerary.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Itineraries.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItineraryDetailsDTO.class)
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
    public ResponseEntity<ItineraryDetailsDTO> itinerary(HttpServletRequest request,
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

        itinerary.setViews(itinerary.getViews() + 1);
        itineraryService.save(itinerary);

        ItineraryDetailsDTO itineraryDetailsDTO = new ItineraryDetailsDTO(itinerary,
                placeService.findFromItinerary(itinerary.getId(), PageRequest.of(placesPage, 10)),
                reviewService.findFromItinerary(itinerary.getId(), PageRequest.of(reviewsPage, 10)));

        return ResponseEntity.ok(itineraryDetailsDTO);
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
                                                                @Parameter(description = "id of the itinerary to view places of")
                                                                @PathVariable long id,
                                                                @Parameter(description = "places page number")
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

    @Operation(summary = "Shows detailed information about an itinerary's reviews")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Itinerary's reviews obtained successfully",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Review.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Operation not available on private itineraries not owned by the current logged in user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            )
    })

    @GetMapping("/{id}/reviews")
    public ResponseEntity<Page<Review>> getReviewsInItinerary(HttpServletRequest request,
                                                              @Parameter(description = "id of the itinerary to view reviews of")
                                                              @PathVariable long id,
                                                              @Parameter(description = "reviews page number")
                                                              @RequestParam(defaultValue = "0") int page) {
        Principal userPrincipal = request.getUserPrincipal();
        Optional<Itinerary> optionalItinerary = itineraryService.findById(id);
        if (!optionalItinerary.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Itinerary itinerary = optionalItinerary.get();
        if (!itinerary.isPublic()) {
            if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            if (!itinerary.getUser().getUsername().equals(userPrincipal.getName()) && !userService.findByUsername(userPrincipal.getName()).get().getRoles().contains("ADMIN")) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(reviewService.findFromItinerary(id, PageRequest.of(page, 10)), HttpStatus.OK);
    }
}
