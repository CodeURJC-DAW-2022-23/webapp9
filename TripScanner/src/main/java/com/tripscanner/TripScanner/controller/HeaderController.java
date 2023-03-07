package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.model.*;
import org.springframework.ui.Model;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.PlaceService;
import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class HeaderController {

        @Autowired
        private UserService userService;

        @ModelAttribute("logIn")
        public Boolean logIn(HttpServletRequest request) {
                Principal user = request.getUserPrincipal();
                if (user != null){
                        return true;
                }else{
                        return false;
                }
        }

        @ModelAttribute("admin")
        public Boolean admin(HttpServletRequest request) {
                Boolean isAdmin = request.isUserInRole("ADMIN");
                return isAdmin;
        }

        @ModelAttribute("search")
        public Boolean search(HttpServletRequest request) {
                String currentPage = request.getRequestURL().toString();
                if (currentPage.contains("search")){
                        return false;
                }else{
                        return true;
                }
        }
}
