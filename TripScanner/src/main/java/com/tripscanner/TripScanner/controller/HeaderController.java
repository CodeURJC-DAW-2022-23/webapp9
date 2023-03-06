package com.tripscanner.TripScanner.controller;

import com.tripscanner.TripScanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

}
