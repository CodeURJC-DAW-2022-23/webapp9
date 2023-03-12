package com.tripscanner.TripScanner.utils;

import com.tripscanner.TripScanner.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements AbstractEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendRegistrationEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(String.format("Welcome to TripScanner, %s %s.\n\n" +
                        "You have finally completed your sign up process for your account with username %s.\n\n" +
                        "If you need any further information, don't hesitate to contact us on %s.",
                user.getFirstName(), user.getLastName(), user.getUsername(), sender));
        mailMessage.setSubject("Welcome to TripScanner!");

        // Sending the mail
        javaMailSender.send(mailMessage);
    }

    public void sendEmailChangeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText(String.format("Changes in your profile.\n\n" +
                        "Dear %s, you have recently changed the email associated to your account.\n\n" +
                        "This email is just to confirm this change. If you need help or information,\n\n" +
                        "please contact us at %s.",
                user.getUsername(), sender));
        mailMessage.setSubject("Changes in your profile's email.");

        // Sending the mail
        javaMailSender.send(mailMessage);
    }

}
