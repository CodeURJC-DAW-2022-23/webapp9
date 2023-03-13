package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
public class PlaceWebController {

    @Autowired
    private PlaceService placeService;

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/place/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Place> place = placeService.findById(id);
        if (place.isPresent() && place.get().getImageFile() != null) {

            Resource file = new InputStreamResource(place.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(place.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("search/place/{id}/image")
    public ResponseEntity<Object> downloadImageSearch(@PathVariable long id) throws SQLException {

        Optional<Place> place = placeService.findById(id);
        if (place.isPresent() && place.get().getImageFile() != null) {

            Resource file = new InputStreamResource(place.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(place.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/management/place/delete/{id}")
    public String deletePlace(Model model, @PathVariable long id) {
        Optional<Place> place = placeService.findById(id);
        for (int i = 0; i < place.get().getItineraries().size(); i++) {
            place.get().getItineraries().get(i).getPlaces().remove(place.get());
            itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().remove(place);
            if (itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().isEmpty()) {
                itineraryService.delete(place.get().getItineraries().get(i).getId());
            }
        }
        placeService.delete(id);
        return "redirect:/management/place/";
    }

    @GetMapping("/management/place/edit/{id}")
    public String editPlaceIni(Model model, @PathVariable long id) {
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("mode", "edit");
        model.addAttribute("edit", true);
        model.addAttribute("type", "Place");
        model.addAttribute("add", false);
        model.addAttribute("place", place.get());
        if (place.get().getDestination() != null) {
            model.addAttribute("dest", place.get().getDestination().getName());
        } else {
            model.addAttribute("dest", " ");
        }

        return "addEditItem";
    }

    @PostMapping("/management/place/edit/{id}")
    public String editPlace(Model model, MultipartFile imageFile, @PathVariable long id, @RequestParam String name, @RequestParam String description, @RequestParam String destination) throws IOException {
        Optional<Place> place = placeService.findById(id);
        place.get().setName(name);
        place.get().setDescription(description);
        Optional<Destination> dest = destinationService.findByName(destination);
        place.get().setDestination(dest.get());
        if (imageFile != null) {
            place.get().setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }
        placeService.save(place.get());
        return "redirect:/management/place/";
    }

    @GetMapping("/management/place/add")
    public String addPlaceIni(Model model) {
        model.addAttribute("mode", "add");
        model.addAttribute("id", "");
        model.addAttribute("add", true);
        model.addAttribute("edit", false);
        model.addAttribute("type", "Place");
        model.addAttribute("place", true);
        model.addAttribute("name", "");
        model.addAttribute("description", "");
        model.addAttribute("dest", "");
        return "addEditItem";
    }

    @PostMapping("/management/place/add")
    public String addPlace(Model model, @RequestParam String name, @RequestParam String description, @RequestParam String destination, @RequestParam MultipartFile imageFile) throws IOException {
        Place place = new Place(name, description, destinationService.findByName(destination).get());
        place.setViews(0L);
        place.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        placeService.save(place);
        return "redirect:/management/place/";
    }

    @GetMapping("/place/{id}/information")
    public String getInformation(Model model, @PathVariable long id, @RequestParam(defaultValue="0") int page) {

        model.addAttribute("itemId", id);
        List<Itinerary> itineraries = placeService.findById(id).get().getItineraries();
        model.addAttribute("information",
                itineraries.subList(Math.min(page * 10, itineraries.size()), Math.min((page + 1) * 10, itineraries.size())));

        return "detailsInformation";
    }

}
