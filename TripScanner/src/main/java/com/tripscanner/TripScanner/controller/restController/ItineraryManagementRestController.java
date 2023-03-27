package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDTO;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.UserService;
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
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/itineraries")
public class ItineraryManagementRestController {
    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;


    @Operation(summary = "Get all itineraries")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the itinerary",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Itinerary not found",
                    content = @Content
            )
    })

    @GetMapping("")
    public ResponseEntity<Page<Itinerary>> getItinerary(@Parameter(description = "page number") @RequestParam(defaultValue = "0") int page) {
        Page<Itinerary> itinerary = itineraryService.findAll(PageRequest.of(page, 10));
        if (!itinerary.isEmpty()) {
            return ResponseEntity.ok(itinerary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Create a new itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Itinerary is created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            )
    })

    @PostMapping("")
    public ResponseEntity<Itinerary> createNewItinerary(@Parameter(description = "new itinerary") @RequestBody ItineraryDTO itinerary) throws IOException {

        Optional<User> user = userService.findByUsername(itinerary.getUser());
        if (user.isEmpty() || itinerary.getName() == null)
            return ResponseEntity.badRequest().build();

        if (itinerary.getDescription() == null)
            itinerary.setDescription("");

        Itinerary newItinerary = new Itinerary(itinerary, user.get(), user.get().getRoles().contains("ADMIN"));

        newItinerary.setImage(true);
        Resource image = new ClassPathResource("static/img/placeholder.jpeg");
        newItinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));

        itineraryService.save(newItinerary);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(newItinerary.getId()).toUri();
        return ResponseEntity.created(location).body(newItinerary);
    }


    @Operation(summary = "Edit itinerary")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Itinerary is edited",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
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
    public ResponseEntity editItinerary(@Parameter(description = "edited item") @PathVariable long id, @RequestBody ItineraryDTO newItineraries) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            if (newItineraries.hasName()) itinerary.get().setName(newItineraries.getName());
            if (newItineraries.hasDescription()) itinerary.get().setDescription(newItineraries.getDescription());
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

    @Operation(summary = "Delete itinerary by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "itinerary is deleted",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Itinerary.class)
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
    public ResponseEntity deleteItinerary(@Parameter(description = "delete itinerary by id") @PathVariable long id) {
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        if (itinerary.isPresent()) {
            itineraryService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Edit itinerary`s image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Image is edited",
                    content = {@Content(
                            mediaType = "image/jpeg",
                            schema = @Schema(implementation = Itinerary.class)
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
    public ResponseEntity<Resource> editImage(@Parameter(description = "edit image") @PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException, SQLException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);

        if (itinerary.isPresent()) {
            Itinerary newitinerary = itinerary.get();
            newitinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            newitinerary.setImage(true);
            itineraryService.save(newitinerary);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/itineraries/" + id + "/image");
            Resource file = new InputStreamResource(imageFile.getInputStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg", HttpHeaders.CONTENT_LOCATION, location.toString())
                    .contentLength(newitinerary.getImageFile().length()).body(file);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
