package com.tripscanner.TripScanner.repository;

import java.util.Optional;

import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;
import org.springframework.data.jpa.repository.Query;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

    @Query("SELECT i from Itinerary i WHERE i.isPublic = true AND (LOWER(i.name) LIKE %:name% OR LOWER(i.description) LIKE %:description%)")
    Page<Itinerary> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);

    @Query("SELECT i from Itinerary i WHERE i.isPublic = true")
    Page<Itinerary> findAllPublic(Pageable pageable);

    Page<Itinerary> findAll(Pageable pageable);

    @Query("SELECT i FROM Itinerary i JOIN i.places p WHERE p.id = :id")
    Page<Itinerary> findFromPlace(long id, Pageable pageable);

    @Query("SELECT i FROM Itinerary i WHERE i.user.id = :id")
    Page<Itinerary> findFromUser(long id, Pageable pageable);


}
