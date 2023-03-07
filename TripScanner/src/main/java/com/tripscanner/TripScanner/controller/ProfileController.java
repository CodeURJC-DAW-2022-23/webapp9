package com.tripscanner.TripScanner.controller;
import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        Optional<User> currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        model.addAttribute("userInfo", currentUser.get());
        model.addAttribute("imageFile", currentUser.get().getImageFile());

        return "profile";
    }
}
