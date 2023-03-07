package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class HeaderController {

        @Autowired
        private UserService userService;

        @ModelAttribute("logIn")
        public Boolean logIn(Model model, HttpServletRequest request) {
                Principal user = request.getUserPrincipal();

                if (user == null){
                        return false;
                }else{
                        model.addAttribute("id", userService.findByUsername(user.getName()).get().getId());
                        return true;
                }
        }

        @ModelAttribute("admin")
        public Boolean admin(HttpServletRequest request) {
                Boolean isAdmin = request.isUserInRole("ADMIN");
                return isAdmin;
        }

}