package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.rest.PlaceDetailsDTO;
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
@RequestMapping("/api/places")
public class PlaceRestController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @Operation(summary = "Searches for Places in the database. Filters can be applied optionally.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Places.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Place.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            )
    })
    @GetMapping("")
    public ResponseEntity<Page<Place>> places(@Parameter(description="search query") @RequestParam(defaultValue = "") String name,
                                              @Parameter(description="sorting type: id, name, views") @RequestParam(defaultValue = "id") String sort,
                                              @Parameter(description="order specifier") @RequestParam(defaultValue = "DESC") String order,
                                              @Parameter(description="page number") @RequestParam(defaultValue = "0") int page) {

        Sort.Direction direction;
        if (Objects.equals(order, "DESC")) direction = Sort.Direction.DESC;
        else if (Objects.equals(order, "ASC")) direction = Sort.Direction.ASC;
        else return ResponseEntity.badRequest().build();

        name = name.toLowerCase();

        return ResponseEntity.ok(placeService.findAllByNameOrDescriptionLikeIgnoreCase(name, name,
                PageRequest.of(page, 10, Sort.by(direction, sort))));
    }

    @Operation(summary = "Searches for Places in the database. Filters can be applied optionally.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully searched the desired Place.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlaceDetails.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments.",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlaceDetailsDTO> place(@Parameter(description="place id") @PathVariable int id,
                                                 @Parameter(description="itineraries page number") @RequestParam(defaultValue = "0") int page) {
        Optional<Place> optionalPlace = placeService.findById(id);

        if (optionalPlace.isPresent()) {
            Place place = optionalPlace.get();
            
            place.setViews(place.getViews() + 1);
            placeService.save(place);

            PlaceDetailsDTO placeDetailsDTO = new PlaceDetailsDTO(place,
                    itineraryService.findFromPlace(place.getId(), PageRequest.of(placesPage, 10)));

            return ResponseEntity.ok(placeDetailsDTO);
        }
        else return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Returns the image of the desired Place.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucessfully returned the Place's image.",
                    content = {@Content(
                            mediaType = "image/jpeg"
                    )}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Requested a non-existing Place's image.",
                    content = @Content
            )
    })
    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> downloadImage(@Parameter(description="place id") @PathVariable long id) throws SQLException {
        Optional<Place> place = placeService.findById(id);

        if (place.isPresent() && place.get().getImageFile() != null) {
            Resource file = new InputStreamResource(place.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(place.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
