package com.tripscanner.TripScanner.service;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.repository.DestinationRepository;
import com.tripscanner.TripScanner.repository.ItineraryRepository;
import com.tripscanner.TripScanner.repository.PlaceRepository;
import com.tripscanner.TripScanner.repository.ReviewRepository;
import com.tripscanner.TripScanner.repository.UserRepository;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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

    @PostConstruct
    public void init() throws IOException, URISyntaxException {

        // To-Do: Add samples for all models

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

    public void setImage(User user, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        user.setImage(true);

    }

}