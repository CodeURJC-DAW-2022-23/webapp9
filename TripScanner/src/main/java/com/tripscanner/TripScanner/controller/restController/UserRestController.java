package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.model.rest.UserDTO;
import com.tripscanner.TripScanner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Create a new user account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
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
                    responseCode = "201",
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
