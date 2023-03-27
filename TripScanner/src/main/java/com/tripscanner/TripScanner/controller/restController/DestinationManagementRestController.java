package com.tripscanner.TripScanner.controller.restController;


import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/management/destinations")
public class DestinationManagementRestController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private DestinationService destinationService;

    @Operation(summary = "Get all destinations")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the destination",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Destination.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Destination not found",
                    content = @Content
            )
    })


    @GetMapping("")
    public ResponseEntity<Page<Destination>> getDestination(@Parameter(description = "page number") @RequestParam(defaultValue = "0") int page) {
        Page<Destination> destinations = destinationService.findAll(PageRequest.of(page, 10));
        if (!destinations.isEmpty()) {
            return ResponseEntity.ok(destinations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new destination")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Destination is created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Destination.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            )
    })

    @PostMapping("")
    public ResponseEntity<Destination> createDestination(@Parameter(description = "new destination") @RequestBody Destination destination) {
        destination.setViews(0L);
        destinationService.save(destination);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(destination.getId()).toUri();
        return ResponseEntity.created(location).body(destination);
    }


    @Operation(summary = "Edit destination")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Destination is edited",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Destination.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Destination not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}")
    public ResponseEntity<Destination> editDestination(@Parameter(description = "edited destination") @PathVariable long id, @RequestBody Destination newDestination) throws SQLException {
        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent()) {
            if (newDestination.getImageFile() != null) {
                newDestination.setImageFile(BlobProxy.generateProxy(destination.get().getImageFile().getBinaryStream(), destination.get().getImageFile().length()));
            }
            newDestination.setId(id);
            newDestination.setViews(destination.get().getViews());
            destinationService.save(newDestination);
            return ResponseEntity.ok(destination.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete destination by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Destination is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Destination.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Destination not found",
                    content = @Content
            )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Destination> deleteDestination(@Parameter(description = "if of destination to delete") @PathVariable long id) {
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


    @Operation(summary = "Edit destination`s image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image is edited",
                    content = {@Content(
                            mediaType = "image/jpeg",
                            schema = @Schema(implementation = Destination.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Image not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}/image")
    public ResponseEntity<Resource> editImage(@Parameter(description = "edited image") @PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException, SQLException {
        Optional<Destination> destination = destinationService.findById(id);

        if (destination.isPresent()) {
            Destination newDestination = destination.get();

            newDestination.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            newDestination.setImage(true);
            destinationService.save(newDestination);

            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            Resource file = new InputStreamResource(imageFile.getInputStream());
            URI location = new URI(baseUrl + "/api/destinations/" + id + "/image");
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg", HttpHeaders.CONTENT_LOCATION, location.toString())
                    .contentLength(newDestination.getImageFile().length()).body(file);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
