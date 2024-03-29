package com.tripscanner.TripScanner.controller.webController;

import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.service.UserService;
import com.tripscanner.TripScanner.utils.EmailService;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/profile")
    public String profile(Model model, HttpServletRequest request) {
        Optional<User> currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        model.addAttribute("userInfo", currentUser.get());
        model.addAttribute("edit", false);
        model.addAttribute("show", true);
        model.addAttribute("goodEmail", true);
        model.addAttribute("uniqueUsername", true);
        model.addAttribute("imageFile", currentUser.get().getImageFile());

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileInit(Model model, HttpServletRequest request) {
        Optional<User> currentUser = userService.findByUsername(request.getUserPrincipal().getName());

        model.addAttribute("userInfo", currentUser.get());
        model.addAttribute("edit", true);
        model.addAttribute("show", false);
        model.addAttribute("goodEmail", true);
        model.addAttribute("uniqueUsername", true);
        model.addAttribute("imageFile", currentUser.get().getImageFile());

        return "profile";
    }

    @PostMapping("profile/edit")
    public String editProfile(Model model,
                              HttpServletRequest request,
                              @RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam String username,
                              @RequestParam String nationality,
                              @RequestParam String email) throws ServletException {
        User currentUser = userService.findByUsername(request.getUserPrincipal().getName()).get();

        if (!email.matches("\\w*@\\w*\\.[a-z]{1,3}")) {
            model.addAttribute("userInfo", currentUser);
            model.addAttribute("edit", true);
            model.addAttribute("show", false);
            model.addAttribute("goodEmail", false);
            model.addAttribute("uniqueUsername", true);
            return "redirect:/profile/edit";
        }

        if (!userService.findByUsername(username).isEmpty() && !currentUser.getUsername().equals(username)) {
            model.addAttribute("userInfo", currentUser);
            model.addAttribute("edit", true);
            model.addAttribute("show", false);
            model.addAttribute("goodEmail", true);
            model.addAttribute("uniqueUsername", false);
            return "redirect:/profile/edit";
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setUsername(username);
        currentUser.setNationality(nationality);
        if (currentUser.getEmail() != email) {
            currentUser.setEmail(email);
            emailService.sendEmailChangeEmail(currentUser);
        }


        userService.save(currentUser);
        request.logout();
        return "redirect:/profile";
    }

    @PostMapping("/profile/edit/photo")
    public String editPhoto(@RequestParam MultipartFile imageFile,
                            HttpServletRequest request,
                            Model model) throws IOException {
        User currentUser = userService.findByUsername(request.getUserPrincipal().getName()).get();

        if (!imageFile.getOriginalFilename().isBlank()) {
            currentUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            userService.save(currentUser);
            model.addAttribute("imageFile", currentUser.getImageFile());
        }
        return "redirect:/profile";
    }
}
