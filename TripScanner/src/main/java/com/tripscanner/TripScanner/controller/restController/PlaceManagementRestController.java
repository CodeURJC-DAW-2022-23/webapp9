package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.PlaceDTO;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.sql.SQLException;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/places")
public class PlaceManagementRestController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Operation(summary = "Get a page with all places")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Places page",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Places not found",
                    content = @Content
            )
    })

    @GetMapping("")
    public ResponseEntity<Page<Place>> getPlaces(
            @Parameter(description="page number")
            @RequestParam(defaultValue = "0") int page){
        Page<Place> places = placeService.findAll(PageRequest.of(page, 10));
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new place")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "New place was created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            )
    })

    @PostMapping("")
    public ResponseEntity<Place> createPlace(
            @Parameter(description="new page's information")
            @RequestBody PlaceDTO newPlace) {
        Place place = new Place(newPlace.getName(), newPlace.getDescription(), destinationService.findByName(newPlace.getDestination()).get());
        placeService.save(place);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(place.getId()).toUri();
        return ResponseEntity.created(location).body(place);
    }

    @Operation(summary = "Edit existing place")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place was correctly edited",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Place not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}")
    public ResponseEntity<Place> editPlace(
            @Parameter(description="id of place to be edited")
            @PathVariable long id,
            @Parameter(description="place's new information")
            @RequestBody PlaceDTO newPlaceDTO) throws SQLException {
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

    @Operation(summary = "Delete place")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place was correctly deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Place not found",
                    content = @Content
            )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Place> deletePlace(
            @Parameter(description="id of place to be deleted")
            @PathVariable long id) {
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

    @Operation(summary = "Upload new place's image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place's image was uploaded correctly",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Place not found",
                    content = @Content
            )
    })

    @PostMapping("/{id}/image")
    public ResponseEntity<Place> uploadImage(
            @Parameter(description="id of place to upload its image")
            @PathVariable long id,
            @Parameter(description="image to be uploaded")
            @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<Place> place = placeService.findById(id);

        if (place.isPresent()) {
            Place newPlace = place.get();

            newPlace.setImage(true);
            newPlace.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));

            placeService.save(newPlace);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/places/" + id + "/image");
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit existing place's image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Place's image was edited correctly",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation= Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Place not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}/image")
    public ResponseEntity<Place> editImage(
            @Parameter(description="id of place to edit its image")
            @PathVariable long id,
            @Parameter(description="image to be uploaded")
            @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<Place> place = placeService.findById(id);

        if (place.isPresent()) {
            Place newPlace = place.get();
            newPlace.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            placeService.save(newPlace);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/places/" + id + "/image");
            return ResponseEntity.created(location).body(newPlace);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
