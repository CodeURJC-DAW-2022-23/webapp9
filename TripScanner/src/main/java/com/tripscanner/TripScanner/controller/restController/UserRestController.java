package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDetails;
import com.tripscanner.TripScanner.model.rest.UserDetails;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ReviewService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.tripscanner.TripScanner.model.rest.UserDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.net.URI;
import java.util.Arrays;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    public UserService userService;

    @Autowired
    public ItineraryService itineraryService;

    @Autowired
    public ReviewService reviewService;

    @Autowired
    EmailService emailService;

    @Autowired
    public PlaceService placeService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<UserDetails> getUser(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        Principal currUser = request.getUserPrincipal();

        if (currUser != null) {
            User user = userService.findByUsername(currUser.getName()).get();
            return new ResponseEntity<>(new UserDetails(user,
                                                        itineraryService.findAllByUsername(currUser.getName(), PageRequest.of(page, 10)),
                                                        reviewService.findFromUser(user.getId(), PageRequest.of(page, 10))), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/itineraries")
    public ResponseEntity<List<ItineraryDetails>> getUserItineraries(HttpServletRequest request,
                                                                     @RequestParam(defaultValue = "0") int pagePlaces,
                                                                     @RequestParam(defaultValue = "0") int pageReviews) {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Page<Itinerary> itineraries = itineraryService.findAllByUsername(currUser.getName(), PageRequest.of(0, 5));

        List<ItineraryDetails> toShow = new ArrayList<>();
        for (Itinerary i : itineraries) {
            toShow.add(new ItineraryDetails(
                    i,
                    placeService.findFromItinerary(i.getId(), PageRequest.of(pagePlaces, 10)),
                    reviewService.findFromItinerary(i.getId(), PageRequest.of(pageReviews, 10))));
        }

        return new ResponseEntity<>(toShow, HttpStatus.OK);
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

    @PutMapping("")
    public ResponseEntity editUser(@RequestBody User newData, HttpServletRequest request) throws ServletException {
        Principal currUser = request.getUserPrincipal();
        if (currUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);
        if (newData == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

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
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/image")
    public ResponseEntity editImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) throws IOException {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(currUser.getName()).get();
        user.setImage(true);
        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> register(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);



        if (user.getUsername() == null || user.getEmail() == null || user.getPasswordHash() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!user.getEmail().matches("\\w*@\\w*\\.[a-z]{1,3}")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!userService.existName(user.getUsername())) {
            user.setImage(false);
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
            user.setRoles(Arrays.asList("USER"));

            userService.save(user);
            URI location = fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

            return ResponseEntity.created(location).body(user);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent() && optionalUser.get().getImageFile() != null) {
            Resource file = new InputStreamResource(optionalUser.get().getImageFile().getBinaryStream());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(optionalUser.get().getImageFile().length()).body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
