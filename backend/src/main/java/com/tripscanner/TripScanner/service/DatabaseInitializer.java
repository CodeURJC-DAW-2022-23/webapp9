package com.tripscanner.TripScanner.service;

import com.tripscanner.TripScanner.model.*;
import com.tripscanner.TripScanner.repository.*;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

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

        Destination destination4 = new Destination("Istanbul", "It is the largest city in Turkey, serving as the country's economic, cultural and historic hub. The city straddles the Bosporus strait, lying in both Europe and Asia, and has a population of over 15 million residents, comprising 19% of the population of Turkey. Istanbul is the most populous European city, and the world's 15th-largest city.", "tr");
        setImage(destination4, "/img-samples/istanbul.jpeg");
        destinationRepository.save(destination4);

        Destination destination5 = new Destination("Beijing", "Beijing is the capital of the People's Republic of China. With over 21 million residents, Beijing is the world's most populous national capital city and is China's second largest city after Shanghai.It is located in Northern China, and is governed as a municipality under the direct administration of the State Council with 16 urban, suburban, and rural districts. Beijing is mostly surrounded by Hebei Province with the exception of neighboring Tianjin to the southeast; together, the three divisions form the Jingjinji megalopolis and the national capital region of China.", "cn");
        setImage(destination5, "/img-samples/Beijing.jpeg");
        destinationRepository.save(destination5);

        Destination destination6 = new Destination("Rome", " s the capital city of Italy. It is also the capital of the Lazio region, the centre of the Metropolitan City of Rome, and a special comune named Comune di Roma Capitale. With 2,860,009 residents in 1,285 km2 (496.1 sq mi),Rome is the country's most populated comune and the third most populous city in the European Union by population within city limits. The Metropolitan City of Rome, with a population of 4,355,725 residents, is the most populous metropolitan city in Italy. Its metropolitan area is the third-most populous within Italy.", "it");
        setImage(destination6, "/img-samples/roma.jpeg");
        destinationRepository.save(destination6);



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

        Place place5 = new Place("The Kremlin in Izmailovo", "The Kremlin in Izmailovo is a cultural and entertainment complex built in 1998-2007 near the historic royal estate of Izmailovo. It is a wooden building, stylized Russian architecture of the XVI—XVII centuries (neo-historicism). Russian Russian Navy Museum, the Museum of the History of Russian Vodka, the Museum of Bread, the Museum of miniatures World History in plasticine, the club — museum — lecture hall Traditional masks and figures of the World, the Moscow Museum of Animation As of 2018, the Kremlin in Izmailovo consists of nine museums and exhibition venues: the Museum of Russian Folk toys, the Museum of the Foundation of the Russian Fleet, the Museum of the History of Russian Vodka, the Museum of Bread, the Museum of miniatures World History in Plasticine, the club - museum - lecture hall.Traditional masks and figures of the World, the Moscow Museum of Animation.", destination3);
        setImage(place5, "/img-samples/izmaylovskiy-kreml.jpeg");
        placeRepository.save(place5);

        Place place6 = new Place("Cathedral of Christ the Saviour", "Cathedral of Christ the Saviour is a Russian Orthodox cathedral in Moscow, Russia, on the northern bank of the Moskva River, a few hundred metres southwest of the Kremlin. With an overall height of 103 metres (338 ft), it is the third tallest Orthodox Christian church building in the world, after the People's Salvation Cathedral in Bucharest, Romania and Saints Peter and Paul Cathedral in Saint Petersburg, Russia. ", destination3);
        setImage(place6, "/img-samples/Cathedral-of-Christ-the-Savior.jpeg");
        placeRepository.save(place6);

        Place place7 = new Place("Hagia Sophia", "Hagia Sophia officially the Hagia Sophia Grand Mosque (Turkish: Ayasofya Camii), is a mosque and major cultural and historical site in Istanbul, Turkey. The mosque was originally built as an Eastern Orthodox church and was used as such from the year 360 until the conquest of Constantinople by the Ottoman Empire in 1453. It served as a mosque until 1935, when it became a museum. In 2020, the site once again became a mosque.", destination4);
        setImage(place7, "/img-samples/Ajya-Sofiya.jpeg");
        placeRepository.save(place7);

        Place place8 = new Place("Grand Bazaar, Istanbul", "The Grand Bazaar in Istanbul is one of the largest and oldest covered markets in the world, with 61 covered streets and over 4,000 shops on a total area of 30,700 m2, attracting between 250,000 and 400,000 visitors daily. In 2014, it was listed No.1 among the world's most-visited tourist attractions with 91,250,000 annual visitors. The Grand Bazaar at Istanbul is often regarded as one of the first shopping malls of the world.", destination4);
        setImage(place8, "/img-samples/grand-bazaar.jpeg");
        placeRepository.save(place8);

        Place place9 = new Place("Blue Mosque", "The Blue Mosque in Istanbul, also known by its official name, the Sultan Ahmed Mosque (Turkish: Sultan Ahmet Camii), is an Ottoman-era historical imperial mosque located in Istanbul, Turkey. A functioning mosque, it also attracts large numbers of tourist visitors. It was constructed between 1609 and 1616 during the rule of Ahmed I. Its Külliye contains Ahmed's tomb, a madrasah and a hospice. Hand-painted blue tiles adorn the mosque’s interior walls, and at night the mosque is bathed in blue as lights frame the mosque’s five main domes, six minarets and eight secondary domes.", destination4);
        setImage(place9, "/img-samples/blue-mosque.jpeg");
        placeRepository.save(place9);

        Place place10 = new Place("Forbidden City", "The Forbidden City is a palace complex in Dongcheng District, Beijing, China, at the center of the Imperial City of Beijing. It is surrounded by numerous opulent imperial gardens and temples including the 22 ha (54-acre) Zhongshan Park, the sacrificial Imperial Ancestral Temple, the 69 ha (171-acre) Beihai Park, and the 23 ha (57-acre) Jingshan Park. It is officially administered by the Palace Museum.", destination5);
        setImage(place10, "/img-samples/forbidden-city.jpeg");
        placeRepository.save(place10);

        Place place11 = new Place("Tiananmen Square", "Tiananmen Square s a city square in the city center of Beijing, China, named after the eponymous Tiananmen (Gate of Heavenly Peace) located to its north, which separates it from the Forbidden City. The square contains the Monument to the People's Heroes, the Great Hall of the People, the National Museum of China, and the Mausoleum of Mao Zedong. Mao Zedong proclaimed the founding of the People's Republic of China in the square on October 1, 1949; the anniversary of this event is still observed there. The size of Tiananmen Square is 765 x 282 meters (215,730 m2 or 53.31 acres). ", destination5);
        setImage(place11, "/img-samples/tienanmen.jpeg");
        placeRepository.save(place11);

        Place place12 = new Place("Colosseum", "The Colosseum s an elliptical amphitheatre in the centre of the city of Rome, Italy, just east of the Roman Forum. It is the largest ancient amphitheatre ever built, and is still the largest standing amphitheatre in the world, despite its age. Construction began under the emperor Vespasian (r. 69–79 AD) in 72 and was completed in 80 AD under his successor and heir, Titus (r. 79–81).", destination6);
        setImage(place12, "/img-samples/Colosseum-Rome.jpeg");
        placeRepository.save(place12);

        Place place13 = new Place("Pantheon", "The Pantheon is a former Roman temple and, since 609 AD, a Catholic church (Basilica di Santa Maria ad Martyres or Basilica of St. Mary and the Martyrs) in Rome, Italy, on the site of an earlier temple commissioned by Marcus Agrippa during the reign of Augustus (27 BC – 14 AD). It was rebuilt by the emperor Hadrian and probably dedicated c. 126 AD.", destination6);
        setImage(place13, "/img-samples/panteon.jpeg");
        placeRepository.save(place13);

        Place place14 = new Place("Trevi Fountain", "The Trevi Fountain is an 18th-century fountain in the Trevi district in Rome, Italy, designed by Italian architect Nicola Salvi and completed by Giuseppe Pannini and several others. Standing 26.3 metres (86 ft) high and 49.15 metres (161.3 ft) wide, it is the largest Baroque fountain in the city and one of the most famous fountains in the world. ", destination6);
        setImage(place14, "/img-samples/fontain.jpeg");
        placeRepository.save(place14);

        Place place15 = new Place("Roman Forum", "The Roman Forum is a rectangular forum (plaza) surrounded by the ruins of several important ancient government buildings at the center of the city of Rome. Citizens of the ancient city referred to this space, originally a marketplace, as the Forum Magnum, or simply the Forum.", destination6);
        setImage(place15, "/img-samples/roman-forum.jpeg");
        placeRepository.save(place15);


        // Sample users
        User user = new User("user", "User1", "User2", "user@example.org", passwordEncoder.encode("pass"), "Spanish", "USER");
        setImage(user, "/static/img/userPhoto.png");
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

        itinerary2.setPlaces(Arrays.asList(place4, place5, place6));
        itineraryRepository.save(itinerary2);

        Itinerary itinerary3 = new Itinerary("Route around Turkey", "Including places in Istanbul", admin, true);
        setImage(itinerary3, "/img-samples/roma.jpeg");

        itinerary3.setPlaces(Arrays.asList(place7, place8, place9));
        itineraryRepository.save(itinerary3);

        Itinerary itinerary4 = new Itinerary("Route around Italy", "Including places in Rome", admin, true);
        setImage(itinerary4, "/img-samples/istanbul.jpeg");

        itinerary4.setPlaces(Arrays.asList(place12, place13, place14, place15));
        itineraryRepository.save(itinerary4);

        Itinerary itinerary5 = new Itinerary("Route around China", "Including places in Beijing", admin, true);
        setImage(itinerary5, "/img-samples/Beijing.jpeg");

        itinerary5.setPlaces(Arrays.asList(place10, place11));
        itineraryRepository.save(itinerary5);


        // Sample reviews

        Review review = new Review("Review", "Nice itinerary around some of the most well-known places in Spain.", 5);
        review.setItinerary(itinerary);
        review.setUser(user);
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