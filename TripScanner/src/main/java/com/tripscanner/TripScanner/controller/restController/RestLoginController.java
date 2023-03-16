package com.tripscanner.TripScanner.controller.restController;

import com.tripscanner.TripScanner.security.jwt.AuthResponse;
import com.tripscanner.TripScanner.security.jwt.LoginRequest;
import com.tripscanner.TripScanner.security.jwt.UserLoginService;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest, accessToken, refreshToken);
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return userService.refresh(refreshToken);
    }
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(request, response)));
    }
}
