package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

    List<Optional<Destination>> findByNameOrDescriptionLikeIgnoreCase(String query);

}
