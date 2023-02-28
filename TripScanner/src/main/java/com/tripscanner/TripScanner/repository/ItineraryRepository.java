package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

    List<Itinerary> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description);

    Page<Itinerary> findAll(Pageable pageable);

}
