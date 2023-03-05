package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.UserService;
import com.tripscanner.TripScanner.service.ItineraryService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class ItineraryWebController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DestinationService destinationService;

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

    @GetMapping("/management/itinerary/delete/{id}")
    public String deleteItinerary(Model model, @PathVariable long id){
        itineraryService.delete(id);
        return "redirect:/management/itinerary/";
    }

    @GetMapping("/management/itinerary/edit/{id}")
    public String editItineraryIni(Model model, @PathVariable long id){
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        model.addAttribute("mode", "edit");
        model.addAttribute("edit", true);
        model.addAttribute("type", "Itinerary");
        model.addAttribute("add", false);
        model.addAttribute("itinerary", itinerary.get());
        if (itinerary.get().getUser() != null){
            model.addAttribute("username", itinerary.get().getUser().getUsername());
        }else{
            model.addAttribute("username", " ");
        }
        return "addEditItem";
    }

    @PostMapping("/management/itinerary/edit/{id}")
    public String editItinerary(Model model, MultipartFile imageFile, @PathVariable long id, @RequestParam String name, @RequestParam String description, @RequestParam String username) throws IOException {
        Optional<Itinerary> itinerary = itineraryService.findById(id);
        itinerary.get().setName(name);
        itinerary.get().setDescription(description);
        Optional<User> userObj = userService.findByUsername(username);
        itinerary.get().setUser(userObj.get());
        if (imageFile != null){
            itinerary.get().setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }
        itineraryService.save(itinerary.get());
        return "redirect:/management/itinerary/";
    }

    @GetMapping("/management/itinerary/add")
    public String addItineraryIni(Model model){
        model.addAttribute("mode", "add");
        model.addAttribute("id", "");
        model.addAttribute("add", true);
        model.addAttribute("edit", false);
        model.addAttribute("type", "Itinerary");
        model.addAttribute("itinerary", true);
        model.addAttribute("name", "");
        model.addAttribute("description", "");
        model.addAttribute("username", "");
        return "addEditItem";
    }

    @PostMapping("/management/itinerary/add")
    public String addItinerary(Model model, @RequestParam String name, @RequestParam String description, @RequestParam String username, @RequestParam MultipartFile imageFile) throws IOException {
        Itinerary itinerary = new Itinerary(name, description, userService.findByUsername(username).get());
        itinerary.setViews(0L);
        itinerary.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        itineraryService.save(itinerary);
        return "redirect:/management/itinerary/";
    }


}
