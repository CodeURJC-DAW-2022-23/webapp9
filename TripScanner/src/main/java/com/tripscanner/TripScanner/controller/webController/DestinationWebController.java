package com.tripscanner.TripScanner.controller.webController;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.hibernate.engine.jdbc.BlobProxy;
import com.tripscanner.TripScanner.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
public class DestinationWebController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/destination/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent() && destination.get().getImageFile() != null) {

            Resource file = new InputStreamResource(destination.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(destination.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("search/destination/{id}/image")
    public ResponseEntity<Object> downloadImageSearch(@PathVariable long id) throws SQLException {

        Optional<Destination> destination = destinationService.findById(id);
        if (destination.isPresent() && destination.get().getImageFile() != null) {

            Resource file = new InputStreamResource(destination.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(destination.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/management/destination/delete/{id}")
    public String deleteDestination(Model model, @PathVariable long id) {
        Optional<Destination> dest = destinationService.findById(id);
        for (int i = 0; i < dest.get().getPlaces().size(); i++) {
            Place place = dest.get().getPlaces().get(i);
            for (int j = 0; j < place.getItineraries().size(); j++) {
                place.getItineraries().get(j).getPlaces().remove(place);
                itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().remove(place);
                if (itineraryService.findById(place.getItineraries().get(j).getId()).get().getPlaces().isEmpty()) {
                    itineraryService.delete(place.getItineraries().get(j).getId());
                }
            }
        }
        destinationService.delete(id);
        return "redirect:/management/destination/";
    }

    @GetMapping("/management/destination/edit/{id}")
    public String editDestinationIni(Model model, @PathVariable long id) {
        Optional<Destination> destination = destinationService.findById(id);
        model.addAttribute("mode", "edit");
        model.addAttribute("edit", true);
        model.addAttribute("type", "Destination");
        model.addAttribute("add", false);
        model.addAttribute("destination", destination.get());
        return "addEditItem";
    }

    @PostMapping("/management/destination/edit/{id}")
    public String editDestination(Model model, MultipartFile imageFile, @PathVariable long id, @RequestParam String name, @RequestParam String description, @RequestParam String flagCode) throws IOException {
        Optional<Destination> destination = destinationService.findById(id);
        destination.get().setName(name);
        destination.get().setDescription(description);
        destination.get().setFlagCode(flagCode);
        if (imageFile != null) {
            destination.get().setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }

        destinationService.save(destination.get());
        return "redirect:/management/destination/";
    }

    @GetMapping("/management/destination/add")
    public String addDestinationIni(Model model) {
        model.addAttribute("mode", "add");
        model.addAttribute("id", "");
        model.addAttribute("add", true);
        model.addAttribute("edit", false);
        model.addAttribute("type", "Destination");
        model.addAttribute("destination", true);
        model.addAttribute("name", "");
        model.addAttribute("description", "");
        model.addAttribute("flagCode", "");
        return "addEditItem";
    }

    @PostMapping("/management/destination/add")
    public String addItinerary(Model model, @RequestParam String name, @RequestParam String description, @RequestParam String flagCode, @RequestParam MultipartFile imageFile) throws IOException {
        Destination destination = new Destination(name, description, flagCode);
        destination.setViews(0L);
        destination.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        destinationService.save(destination);
        return "redirect:/management/destination/";
    }

    @GetMapping("/destination/{id}/information")
    public String getInformation(Model model, @PathVariable long id, @RequestParam(defaultValue = "0") int page) {

        model.addAttribute("itemId", id);
        List<Place> places = destinationService.findById(id).get().getPlaces();
        model.addAttribute("information",
                places.subList(Math.min(page * 10, places.size()), Math.min((page + 1) * 10, places.size())));

        return "detailsInformation";
    }
}
