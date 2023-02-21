package com.tripscanner.TripScanner.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

}
