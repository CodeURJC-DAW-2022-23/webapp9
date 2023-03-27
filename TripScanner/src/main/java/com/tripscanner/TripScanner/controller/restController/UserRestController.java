package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.ItineraryDetails;
import com.tripscanner.TripScanner.model.rest.UserDetails;
import com.tripscanner.TripScanner.service.ItineraryService;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.ReviewService;
import com.tripscanner.TripScanner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping("/me")
    public ResponseEntity<UserDetails> getUser(HttpServletRequest request,
                                               @RequestParam(defaultValue = "0") int pageItineraries,
                                               @RequestParam(defaultValue = "0") int pageReviews) {
        Principal currUser = request.getUserPrincipal();

        if (currUser != null) {
            User user = userService.findByUsername(currUser.getName()).get();
            return new ResponseEntity<>(new UserDetails(user,
                                                        itineraryService.findAllByUsername(currUser.getName(), PageRequest.of(pageItineraries, 10)),
                                                        reviewService.findFromUser(user.getId(), PageRequest.of(pageReviews, 10))), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/me/itineraries")
    public ResponseEntity<Page<Itinerary>> getUserItineraries(HttpServletRequest request,
                                                              @RequestParam(defaultValue = "0") int page) {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Page<Itinerary> itineraries = itineraryService.findAllByUsername(currUser.getName(), PageRequest.of(page, 10));

        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }

    @GetMapping("/me/image")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request) throws SQLException {
        Principal principalUser = request.getUserPrincipal();
        if (principalUser == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        User user = userService.findByUsername(principalUser.getName()).get();
        if (!user.isImage()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Resource img = new InputStreamResource(user.getImageFile().getBinaryStream());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(user.getImageFile().length()).body(img);
    }

    @PutMapping("/me")
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

        return ResponseEntity.ok().body(usr);
    }

    @PutMapping("/me/image")
    public ResponseEntity editImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) throws IOException, SQLException {
        Principal currUser = request.getUserPrincipal();

        if (currUser == null) return new ResponseEntity(HttpStatus.FORBIDDEN);

        User user = userService.findByUsername(currUser.getName()).get();
        user.setImage(true);
        user.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        userService.save(user);
        Resource file = new InputStreamResource(imageFile.getInputStream());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .contentLength(user.getImageFile().length()).body(file);
    }

    @PostMapping("")
    @Operation(summary = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sucessfully signed up the new user",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid arguments or required arguemnts missing",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Username already in use",
                    content = @Content
            )
    })
    public ResponseEntity<User> register(
            @Parameter(description="user details", content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation=UserDTO.class))
            }) @RequestBody UserDTO userDTO) {
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
    @Operation(summary = "Returns the profile image of the desired user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucessfully returned the user image",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Username already in use",
                    content = @Content
            )
    })
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
