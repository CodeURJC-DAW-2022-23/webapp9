package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.security.jwt.AuthResponse;
import com.tripscanner.TripScanner.security.jwt.LoginRequest;
import com.tripscanner.TripScanner.security.jwt.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class RestLoginController {

    @Autowired
    private UserLoginService userService;

    @Operation(summary = "Login to Tripscanner")
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation= AuthResponse.class)
            )}
    )

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginRequest loginRequest) {

        return userService.login(loginRequest, accessToken, refreshToken);
    }

    @Operation(summary = "Refresh your session")
    @ApiResponse(
            responseCode = "200",
            description = "Session refreshed successfully",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation= AuthResponse.class)
            )}
    )

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        return userService.refresh(refreshToken);
    }

    @Operation(summary = "Logout from Tripscanner")
    @ApiResponse(
            responseCode = "200",
            description = "Logout successful",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation= AuthResponse.class)
            )}
    )

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(request, response)));
    }
}