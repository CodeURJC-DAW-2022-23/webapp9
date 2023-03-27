package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.rest.GraphDetails;
import com.tripscanner.TripScanner.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/index")
    public ResponseEntity<GraphDetails> indexGraph() {
        List<Destination> destinations = destinationService.findAll(Sort.by("DESC", "views"));
        List<Long> views = new ArrayList<>();

        for (Destination destination : destinations) {
            views.add(destination.getViews());
        }

        return ResponseEntity.ok(new GraphDetails(destinations, views));
    }

}
