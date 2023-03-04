package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ItineraryService;
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

import javax.swing.text.html.Option;
import java.sql.SQLException;
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

    @GetMapping("/management/place/delete/{id}")
    public String deletePlace(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        for(int i = 0; i < place.get().getItineraries().size(); i++){
            place.get().getItineraries().get(i).getPlaces().remove(place.get());
            itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().remove(place);
            if (itineraryService.findById(place.get().getItineraries().get(i).getId()).get().getPlaces().isEmpty()){
                itineraryService.delete(place.get().getItineraries().get(i).getId());
            }
        }
        placeService.delete(id);
        return "redirect:/management/place/";
    }

    @GetMapping("/management/place/edit/{id}")
    public String editPlaceIni(Model model, @PathVariable long id){
        Optional<Place> place = placeService.findById(id);
        model.addAttribute("mode", "edit");
        model.addAttribute("edit", true);
        model.addAttribute("type", "Place");
        model.addAttribute("add", false);
        model.addAttribute("place", place.get());
        if (place.get().getDestination() != null){
            model.addAttribute("dest", place.get().getDestination().getName());
        }else{
            model.addAttribute("dest", " ");
        }

        return "addEditItem";
    }

    @PostMapping("/management/place/edit/{id}")
    public String editPlace(Model model, @PathVariable long id, @RequestParam String name, @RequestParam String description, @RequestParam String destination){
        Optional<Place> place = placeService.findById(id);
        place.get().setName(name);
        place.get().setDescription(description);
        Optional<Destination> dest = destinationService.findByName(destination);
        place.get().setDestination(dest.get());

        placeService.save(place.get());
        return "redirect:/management/place/";
    }

}
