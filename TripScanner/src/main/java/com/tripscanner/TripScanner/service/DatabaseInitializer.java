package com.tripscanner.TripScanner.service;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Itinerary;
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

        // Sample destination

        Destination destination1 = new Destination(1L, "Madrid", "Capital de España");
        setImage(destination1, "/img-samples/madrid.jpg");
        destinationRepository.save(destination1);

        Destination destination2 = new Destination(0L, "Sevilla", "Provincia de Andalucía");
        setImage(destination1, "/img-samples/sevilla.jpg");
        destinationRepository.save(destination2);

        // Sample places

        Place place1 = new Place(1L, "Puerta del Sol", "Descripción Puerta del Sol");
        setImage(place1, "/img-samples/madrid-sol.jpeg");
        placeRepository.save(place1);

        Place place2 = new Place(2L, "Torre del Oro", "Descripción Torre del Oro");
        setImage(place2, "/img-samples/sevilla-torre-oro.jpeg");
        placeRepository.save(place2);

        Place place3 = new Place(3L, "Puerta del Sol", "Descripción Puerta del Sol");
        setImage(place3, "/img-samples/sevilla-catedral.jpeg");
        placeRepository.save(place3);

        // Sample itineraries

        Itinerary itinerary = new Itinerary(1L, "Ruta por España", "Incluyendo lugares de Madrid y Sevilla");
        itineraryRepository.save(itinerary);

        // Sample users

        userRepository.save(new User("user1", "1name", "2name", "jiji@gmail.com", "pass", "USER"));
        userRepository.save(new User("usernombre", "nombre1", "nombre2", "random@gmail.com", "pass",  "USER", "ADMIN"));
        userRepository.save(new User("ola", "tu", "mama", "tumama@gmail.com", "pass", "USER"));

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

    public void setFlag(Destination destination, String classpathResource) throws IOException {

        Resource flag = new ClassPathResource(classpathResource);

        destination.setFlagFile(BlobProxy.generateProxy(flag.getInputStream(), flag.contentLength()));
        destination.setFlag(true);

    }

}