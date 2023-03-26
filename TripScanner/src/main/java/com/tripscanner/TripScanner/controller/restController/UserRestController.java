package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.UserDTO;
import com.tripscanner.TripScanner.service.UserService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
