package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ItineraryWebController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private PlaceService placeService;

    @GetMapping("/itinerary/{id}/image")
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

    @PostMapping("/itinerary/add/place/{id}")
    public String addPlaceToItinerary(Model model, HttpServletRequest request, List<Itinerary> itineraryList, @PathVariable long id) {

        Optional<Place> place = placeService.findById(id);
        if (place.isEmpty() || itineraryList.isEmpty()) return "redirect:/details/place/"+id;

        for (Itinerary itinerary : itineraryList) {
            Optional<Itinerary> dbItinerary = itineraryService.findById(itinerary.getId());
            if (dbItinerary.isEmpty()) continue;

            dbItinerary.get().getPlaces().add(place.get());
            itineraryService.save(dbItinerary.get());
        }

        return "redirect:/details/itinerary/"+itineraryList.get(itineraryList.size() - 1).getId();
    }

}
