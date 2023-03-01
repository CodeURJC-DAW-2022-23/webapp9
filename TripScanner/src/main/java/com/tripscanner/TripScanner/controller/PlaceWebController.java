package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLException;
import java.util.Optional;

@Controller
public class PlaceWebController {

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping("/place/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Place> place = placeRepository.findById(id);
        if (place.isPresent() && place.get().getImageFile() != null) {

            Resource file = new InputStreamResource(place.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(place.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
