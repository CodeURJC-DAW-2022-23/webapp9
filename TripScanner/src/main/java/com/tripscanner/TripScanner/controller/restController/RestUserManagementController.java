package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.UserDTO;
import com.tripscanner.TripScanner.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/management/users")
public class RestUserManagementController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable){
        Page<User> users = userService.findAll(pageable);
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO);

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

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable long id, @RequestBody UserDTO userDTO) throws SQLException {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            User newUser = new User(userDTO);
            newUser.setImageFile(user.get().getImageFile());
            newUser.setPasswordHash(user.get().getPasswordHash());
            newUser.setRoles(user.get().getRoles());
            newUser.setId(id);
            newUser.setReviews(user.get().getReviews());
            newUser.setItineraries(user.get().getItineraries());

            userService.save(newUser);
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<User> uploadImage(@PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            User newUser = user.get();

            newUser.setImage(true);
            newUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));

            userService.save(newUser);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/users/" + id + "/image");
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<User> editImage(@PathVariable long id, @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            User newUser = user.get();
            newUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            userService.save(newUser);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/users/" + id + "/image");
            return ResponseEntity.created(location).body(newUser);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
