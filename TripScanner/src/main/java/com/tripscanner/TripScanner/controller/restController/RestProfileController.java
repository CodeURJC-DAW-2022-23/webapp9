package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import com.tripscanner.TripScanner.utils.EmailService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class RestProfileController {

    @Autowired
    public UserService userService;

    @Autowired
    public ItineraryService itineraryService;

    @Autowired
    public PlaceService placeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @GetMapping("/")
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        Principal currUser = request.getUserPrincipal();

        if (currUser != null) {
            return new ResponseEntity<>(userService.findByUsername(currUser.getName()).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> currUser = userService.findByUsername(username);

        if (currUser.isPresent()) {
            return new ResponseEntity<>(currUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/itineraries")
    public ResponseEntity<Page<Itinerary>> getUserItineraries(HttpServletRequest request) {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Page<Itinerary> itineraries = itineraryService.findAllByUsername(currUser.getName(), PageRequest.of(0, 5));

        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    @GetMapping("/image")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request) throws SQLException {
        Principal principalUser = request.getUserPrincipal();
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        User user = userService.findByUsername(principalUser.getName()).get();
        if (!user.isImage()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Resource img = new InputStreamResource(user.getImageFile().getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.getImageFile().length()).body(img);
    }
    @GetMapping("/{username}/image")
    public ResponseEntity<Object> downloadProfileImageFromUsername(@PathVariable String username, HttpServletRequest request) throws SQLException {
        Optional<User> optionalUser = userService.findByUsername(username);
        if (!optionalUser.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = optionalUser.get();
        if (!user.isImage()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Resource img = new InputStreamResource(user.getImageFile().getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.getImageFile().length()).body(img);
    }

    @PutMapping("/")
    public ResponseEntity<User> editUser(@RequestBody User newData, HttpServletRequest request) throws ServletException {
        Principal currUser = request.getUserPrincipal();
        if (currUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (newData == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User usr = userService.findByUsername(currUser.getName()).get();
        if (userService.findByUsername(newData.getUsername()).isPresent() && newData.getUsername().equals(usr.getUsername())) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (newData.getUsername() != null && newData.hasUserName()) {
            request.logout();
            usr.setUsername(newData.getUsername());
        }
        if (newData.getFirstName() != null && newData.hasFirstName()) usr.setFirstName(newData.getFirstName());
        if (newData.getLastName() != null && newData.hasLastName()) usr.setLastName(newData.getLastName());
        if (newData.getEmail() != null && !newData.getEmail().equals(usr.getEmail()) && newData.hasEmail()) {
            usr.setEmail(newData.getEmail());
            emailService.sendEmailChangeEmail(usr);
        }

        userService.save(usr);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/image")
    public ResponseEntity<User> editImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) throws IOException {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(currUser.getName()).get();
        user.setImage(true);
        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
