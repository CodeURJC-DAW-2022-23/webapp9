package com.tripscanner.TripScanner.controller.restController;

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
public class UserManagementRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Get a page with all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users page",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Users not found",
                    content = @Content
            )
    })

    @GetMapping("")
    public ResponseEntity<Page<User>> getUsers(
            @Parameter(description="page number")
            @RequestParam(defaultValue = "0") int page){
        Page<User> users = userService.findAll(PageRequest.of(page, 10));
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the specified user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @Parameter(description="id of user")
            @PathVariable long id){
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "New user was created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            )
    })

    @PostMapping("")
    public ResponseEntity<User> createUser(
            @Parameter(description="new user's information")
            @RequestBody UserDTO userDTO) {
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

    @Operation(summary = "Edit existing user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User was correctly edited",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation=User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(
            @Parameter(description="id of user to be edited")
            @PathVariable long id,
            @Parameter(description="user's new information")
            @RequestBody UserDTO userDTO) throws SQLException {
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

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "User was correctly deleted",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(
            @Parameter(description="id of user to be deleted")
            @PathVariable long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.delete(id);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit existing user's image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's image was edited correctly",
                    content = {@Content(
                            mediaType = "image/jpeg",
                            schema = @Schema(implementation=User.class)
                    )}
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })

    @PutMapping("/{id}/image")
    public ResponseEntity<Resource> editImage(
            @Parameter(description="id of user to edit its image")
            @PathVariable long id,
            @Parameter(description="image to be uploaded")
            @RequestParam MultipartFile imageFile, HttpServletRequest request) throws IOException, URISyntaxException, SQLException {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            User newUser = user.get();
            newUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            newUser.setImage(true);
            userService.save(newUser);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request).replacePath(null).build().toUriString();
            URI location = new URI(baseUrl + "/api/users/" + id + "/image");
            Resource file = new InputStreamResource(imageFile.getInputStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,  "image/" + imageFile.getOriginalFilename().split("\\.")[1], HttpHeaders.CONTENT_LOCATION, location.toString())
                    .contentLength(newUser.getImageFile().length()).body(file);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
