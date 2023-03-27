package com.tripscanner.TripScanner.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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

        Destination destination1 = new Destination("Madrid", "Madrid is the capital and most populous city of Spain. The city has almost 3.4 million inhabitants and a metropolitan area population of approximately 6.7 million. It is the second-largest city in the European Union (EU), and its monocentric metropolitan area is the second-largest in the EU. The municipality covers 604.3 km2 (233.3 sq mi) geographical area.\n" +
                "\n" +
                "Madrid lies on the River Manzanares in the central part of the Iberian Peninsula. Capital city of both Spain (almost without interruption since 1561) and the surrounding autonomous community of Madrid (since 1983), it is also the political, economic and cultural centre of the country. The city is situated on an elevated plain about 300 km (190 mi) from the closest seaside location. The climate of Madrid features hot summers and cool winters.", "es");
        setImage(destination1, "/img-samples/madrid.jpg");
        destinationRepository.save(destination1);

        Destination destination2 = new Destination("Seville", "Seville is the capital and largest city of the Spanish autonomous community of Andalusia and the province of Seville. It is situated on the lower reaches of the River Guadalquivir, in the southwest of the Iberian Peninsula.\n" +
                "\n" +
                "Seville has a municipal population of about 685,000 as of 2021, and a metropolitan population of about 1.5 million, making it the largest city in Andalusia, the fourth-largest city in Spain and the 26th most populous municipality in the European Union. Its old town, with an area of 4 square kilometres (2 sq mi), contains three UNESCO World Heritage Sites: the Alcázar palace complex, the Cathedral and the General Archive of the Indies. The Seville harbour, located about 80 kilometres (50 miles) from the Atlantic Ocean, is the only river port in Spain. The capital of Andalusia features hot temperatures in the summer, with daily maximums routinely above 35 °C (95 °F) in July and August. ", "es");
        setImage(destination2, "/img-samples/sevilla.jpg");
        destinationRepository.save(destination2);

        Destination destination3 = new Destination("Moscow", "Moscow is the capital and largest city of Russia. The city stands on the Moskva River in Central Russia, with a population estimated at 13.0 million residents within the city limits, over 17 million residents in the urban area, and over 21.5 million residents in the metropolitan area. The city covers an area of 2,511 square kilometers (970 sq mi), while the urban area covers 5,891 square kilometers (2,275 sq mi), and the metropolitan area covers over 26,000 square kilometers (10,000 sq mi). Moscow is among the world's largest cities; being the most populous city entirely in Europe, the largest urban and metropolitan area in Europe, and the largest city by land area on the European continent.", "ru");
        setImage(destination3, "/img-samples/moscu.jpeg");
        destinationRepository.save(destination3);

        // Sample places

        Place place1 = new Place("Puerta del Sol", "The Puerta del Sol is a public square in Madrid, one of the best known and busiest places in the city. This is the centre (Km 0) of the radial network of Spanish roads. The square also contains the famous clock whose bells mark the traditional eating of the Twelve Grapes and the beginning of a new year. The New Year's celebration has been broadcast live since 31 December 1962 on major radio and television networks including Atresmedia and RTVE. ", destination1);
        setImage(place1, "/img-samples/madrid-sol.jpeg");
        placeRepository.save(place1);

        Place place2 = new Place("Torre del Oro", "The Torre del Oro is a dodecagonal military watchtower in Seville, southern Spain. It was erected by the Almohad Caliphate in order to control access to Seville via the Guadalquivir river.\n" +
                "\n" +
                "Constructed in the first third of the 13th century, the tower served as a prison during the Middle Ages. Its name comes from the golden shine it projected on the river, due to its building materials (a mixture of mortar, lime and pressed hay). ", destination2);
        setImage(place2, "/img-samples/sevilla-torre-oro.jpeg");
        placeRepository.save(place2);

        Place place3 = new Place("Catedral de Sevilla", "The Cathedral of Saint Mary of the See, better known as Seville Cathedral, is a Roman Catholic cathedral in Seville, Andalusia, Spain. It was registered in 1987 by UNESCO as a World Heritage Site, along with the adjoining Alcázar palace complex and the General Archive of the Indies. It is one of the largest churches in the world as well as the largest Gothic church.", destination2);
        setImage(place3, "/img-samples/sevilla-catedral.jpeg");
        placeRepository.save(place3);

        Place place4 = new Place("Red Square", "Red Square is one of the oldest and largest squares in Moscow, the capital of Russia. Owing to its historical significance and the adjacent historical buildings, it is regarded as one of the most famous squares in Europe and the world. It is located in Moscow's historic centre, in the eastern walls of the Kremlin. It is the city landmark of Moscow, with iconic buildings such as Saint Basil's Cathedral, Lenin's Mausoleum and the GUM. In addition, it has been a UNESCO World Heritage Site since 1990. ", destination3);
        setImage(place4, "/img-samples/redSquare.jpeg");
        placeRepository.save(place4);


        // Sample users
        User user = new User("user", "User1", "User2", "user@example.org", passwordEncoder.encode("pass"), "Spanish", "USER");
        userRepository.save(user);
        User admin = new User("admin", "Admin2", "Admin2", "admin@example.org", passwordEncoder.encode("adminpass"), "Spanish", "USER", "ADMIN");
        setImage(admin, "/img-samples/madrid-sol.jpeg");
        userRepository.save(admin);

        // Sample itineraries

        Itinerary itinerary = new Itinerary("Route around Spain", "Including places in Madrid and Seville", user, true);
        setImage(itinerary, "/img-samples/madrid-sol.jpeg");


        itinerary.setPlaces(Arrays.asList(place1, place2, place3));
        itineraryRepository.save(itinerary);

        Itinerary itinerary2 = new Itinerary("Route around Russia", "Including places in Moscow", admin, false);
        setImage(itinerary2, "/img-samples/moscu.jpeg");

        itinerary2.setPlaces(Arrays.asList(place4));
        itineraryRepository.save(itinerary2);


        // Sample reviews

        Review review = new Review("Review", "Nice itinerary around some of the most well-known places in Spain.", 5);
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