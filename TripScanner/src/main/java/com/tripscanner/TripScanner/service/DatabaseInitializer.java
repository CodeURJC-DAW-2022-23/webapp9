package com.tripscanner.TripScanner.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import com.tripscanner.TripScanner.model.*;
import com.tripscanner.TripScanner.repository.DestinationRepository;
import com.tripscanner.TripScanner.repository.ItineraryRepository;
import com.tripscanner.TripScanner.repository.PlaceRepository;
import com.tripscanner.TripScanner.repository.ReviewRepository;
import com.tripscanner.TripScanner.repository.UserRepository;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    private String searchWord;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException, URISyntaxException {

        // Sample destination

        Destination destination1 = new Destination("Madrid", "Capital de España", "es");
        setImage(destination1, "/img-samples/madrid.jpg");
        destinationRepository.save(destination1);

        Destination destination2 = new Destination("Sevilla", "Provincia de Andalucía", "es");
        setImage(destination2, "/img-samples/sevilla.jpg");
        destinationRepository.save(destination2);

        Destination destination3 = new Destination("Moscu", "Provincia de Rusia", "ru");
        setImage(destination3, "/img-samples/moscu.jpeg");
        destinationRepository.save(destination3);

        // Sample places

        Place place1 = new Place("Puerta del Sol", "Descripción Puerta del Sol", destination1);
        setImage(place1, "/img-samples/madrid-sol.jpeg");
        placeRepository.save(place1);

        Place place2 = new Place("Torre del Oro", "Descripción Torre del Oro", destination2);
        setImage(place2, "/img-samples/sevilla-torre-oro.jpeg");
        placeRepository.save(place2);

        Place place3 = new Place("Catedral de Sevilla", "Descripción Catedral de Sevilla", destination2);
        setImage(place3, "/img-samples/sevilla-catedral.jpeg");
        placeRepository.save(place3);

        Place place4 = new Place("Red Square", "Red Square", destination3);
        setImage(place4, "/img-samples/redSquare.jpeg");
        placeRepository.save(place4);


        // Sample users
        userRepository.save(new User("user", "User1", "User2", "user@example.org", passwordEncoder.encode("pass"), "Española", "USER"));
        User admin = new User("admin", "Admin2", "Admin2", "admin@example.org", passwordEncoder.encode("adminpass"), "Española", "USER", "ADMIN");
        setImage(admin, "/img-samples/madrid-sol.jpeg");
        userRepository.save(admin);

        // Sample itineraries

        Itinerary itinerary = new Itinerary("Ruta por España", "Incluyendo lugares de Madrid y Sevilla", admin, true);
        setImage(itinerary, "/img-samples/madrid-sol.jpeg");


        itinerary.setPlaces(Arrays.asList(place1, place2, place3));
        itineraryRepository.save(itinerary);

        Itinerary itinerary2 = new Itinerary("Ruta por Rusia", "Incluyendo lugares de Moscu", admin, false);
        setImage(itinerary2, "/img-samples/moscu.jpeg");

        itinerary2.setPlaces(Arrays.asList(place4));
        itineraryRepository.save(itinerary2);


        // Sample reviews

        Review review = new Review("Review", "Descipción de review", 5);
        review.setItinerary(itinerary);
        reviewRepository.save(review);
    }

    public void setImage(Destination destination, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        destination.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        destination.setImage(true);

    }

    public void setImage(Place place, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        place.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        place.setImage(true);

    }

    public void setImage(Itinerary itinerary, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        itinerary.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        itinerary.setImage(true);

    }

    public void setImage(User user, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        user.setImage(true);

    }

}