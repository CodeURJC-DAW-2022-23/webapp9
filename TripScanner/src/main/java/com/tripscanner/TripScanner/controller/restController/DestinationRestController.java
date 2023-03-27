package com.tripscanner.TripScanner.controller.restController;

import com.fasterxml.jackson.annotation.JsonView;
import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.rest.DestinationDetails;
import com.tripscanner.TripScanner.model.rest.UserDTO;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/destinations")
public class DestinationRestController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Operation(summary = "Searches for destinations in the database. Filters can be applied optionally.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired destinations.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            )
    })
    @GetMapping("")
    public ResponseEntity<Page<Destination>> destinations(@Parameter(description="search query") @RequestParam(defaultValue = "") String name,
                                                          @Parameter(description="sorting type: id, name, views") @RequestParam(defaultValue = "id") String sort,
                                                          @Parameter(description="order specifier") @RequestParam(defaultValue = "DESC") String order,
                                                          @Parameter(description="page number") @RequestParam(defaultValue = "0") int page) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        return ResponseEntity.ok(destinationService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                PageRequest.of(page, 10, Sort.by(direction, sort))));
    }

    @Operation(summary = "Get Detailed information about a specific Destination.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired destinations.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Destination.",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DestinationDetails> destination(@Parameter(description="destination id") @PathVariable int id,
                                                          @Parameter(description="places page number") @RequestParam(defaultValue = "0") int placesPage) {
        Optional<Destination> optionalDestination = destinationService.findById(id);

        if (optionalDestination.isPresent()) {
            Destination destination = optionalDestination.get();
            DestinationDetails destinationDetails = new DestinationDetails(destination,
                    placeService.findFromDestination(destination.getId(), PageRequest.of(placesPage, 10)));

            return ResponseEntity.ok(destinationDetails);
        }
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Returns the image of the desired Destination.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucessfully returned the Destination's image.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Destination's image.",
                    content = @Content
            )
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@Parameter(description="destination id") @PathVariable long id) throws SQLException {
        Optional<Destination> destination = destinationService.findById(id);

        if (destination.isPresent() && destination.get().getImageFile() != null) {
            Resource file = new InputStreamResource(destination.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(destination.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
