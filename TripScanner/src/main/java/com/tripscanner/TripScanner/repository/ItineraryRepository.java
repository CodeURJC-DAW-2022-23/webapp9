package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

    //List<Itinerary> findAllByIdLikeIgnoreCase(long id);

    List<Itinerary> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description);

    Page<Itinerary> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    List<Itinerary> findAllByNameOrDescriptionOrderByName(String name, String description, Pageable pageable);

    Page<Itinerary> findAll(Pageable pageable);


}
