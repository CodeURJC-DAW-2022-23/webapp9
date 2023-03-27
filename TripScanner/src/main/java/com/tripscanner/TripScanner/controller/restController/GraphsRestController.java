package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.rest.GraphDetailsDTO;
import com.tripscanner.TripScanner.service.DestinationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/graphs")
public class GraphsRestController {

    @Autowired
    private DestinationService destinationService;

    @Operation(summary = "Get needed information to build the top 5 destinations graph with its views.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns requested data.",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GraphDetails.class)
                    )}
            )
    })
    @GetMapping("/index")
    public ResponseEntity<GraphDetailsDTO> indexGraph() {
        List<Destination> destinations = destinationService.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "views"))).toList();
        List<Long> views = new ArrayList<>();

        for (Destination destination : destinations) {
            views.add(destination.getViews());
        }

        return ResponseEntity.ok(new GraphDetailsDTO(destinations, views));
    }

}
