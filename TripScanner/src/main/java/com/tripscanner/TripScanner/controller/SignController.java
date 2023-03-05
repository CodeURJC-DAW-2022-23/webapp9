package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/sign")
    public String sign(Model model) {
        model.addAttribute("correct", true);
        model.addAttribute("noEmptyFields", true);
        model.addAttribute("goodEmail", true);

        return "sign";
    }

    @PostMapping("/sign")
    public String newUser(
            Model model,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String userName,
            @RequestParam String nationality,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String repeatedPassword
    ) {
        if (!(repeatedPassword.equals(password))) {
            model.addAttribute("correct", false);
            model.addAttribute("noEmptyFields", true);
            model.addAttribute("goodEmail", true);
            return "sign";
        }

        if (userName.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || repeatedPassword.isBlank()) {
            model.addAttribute("correct", true);
            model.addAttribute("noEmptyFields", false);
            model.addAttribute("goodEmail", true);
            return "sign";
        }

        if (!email.matches("\\w*@\\w*\\.[a-z]{1,3}")) {
            model.addAttribute("correct", true);
            model.addAttribute("noEmptyFields", true);
            model.addAttribute("goodEmail", false);
            return "sign";
        }

        userService.save(new User(
                userName,
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                nationality,
                "USER"
        ));

        return "login";
    }
}