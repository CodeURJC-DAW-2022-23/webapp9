package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.DestinationService;
import com.tripscanner.TripScanner.service.UserService;
import com.tripscanner.TripScanner.service.ItineraryService;
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

import java.sql.SQLException;
import java.util.Optional;

@Controller
public class UserWebController {

    @Autowired
    private ItineraryService itineraryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DestinationService destinationService;

    @GetMapping("/user/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<User> user = userService.findById(id);
        if (user.isPresent() && user.get().getImageFile() != null) {

            Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(user.get().getImageFile().length()).body(file);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/management/user/delete/{id}")
    public String deleteUser(Model model, @PathVariable long id){
        userService.delete(id);
        return "redirect:/management/user/";
    }

    @GetMapping("/management/user/edit/{id}")
    public String editUserIni(Model model, @PathVariable long id){
        Optional<User> user = userService.findById(id);
        model.addAttribute("mode", "edit");
        model.addAttribute("edit", true);
        model.addAttribute("type", "User");
        model.addAttribute("add", false);
        model.addAttribute("user", user.get());
        return "addEditItem";
    }

    @PostMapping("/management/user/edit/{id}")
    public String editUser(Model model, @PathVariable long id, @RequestParam String username, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email){
        Optional<User> user = userService.findById(id);
        user.get().setUsername(username);
        user.get().setFirstName(firstName);
        user.get().setLastName(lastName);
        user.get().setEmail(email);

        userService.save(user.get());
        return "redirect:/management/user/";
    }

    @GetMapping("/management/user/add")
    public String addUserIni(Model model){
        model.addAttribute("mode", "add");
        model.addAttribute("id", "");
        model.addAttribute("add", true);
        model.addAttribute("edit", false);
        model.addAttribute("type", "User");
        model.addAttribute("user", true);
        model.addAttribute("username", "");
        model.addAttribute("firstName", "");
        model.addAttribute("lastName", "");
        model.addAttribute("email", "");
        model.addAttribute("password", "");
        return "addEditItem";
    }

    @PostMapping("/management/user/add")
    public String addUser(Model model, @RequestParam String username, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password){
        User user = new User(username, firstName, lastName, email, password);
        userService.save(user);
        return "redirect:/management/user/";
    }
}
