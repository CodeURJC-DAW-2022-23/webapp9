package com.tripscanner.TripScanner.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Itinerary;
import org.springframework.data.jpa.repository.Query;

public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    Optional<Itinerary> findByName(String name);

    @Query("SELECT i from Itinerary i WHERE i.isPublic = true AND (LOWER(i.name) LIKE %:name% OR LOWER(i.description) LIKE %:description%)")
    Page<Itinerary> findAllByNameOrDescriptionLike(String name, String description, Pageable pageable);

    @Query("SELECT i from Itinerary i WHERE i.isPublic = true")
    Page<Itinerary> findAllPublic(Pageable pageable);

    Page<Itinerary> findAll(Pageable pageable);

    @Query("SELECT i from Itinerary i WHERE i.user.username = :user")
    Page<Itinerary> findAllByUser(String user, Pageable pageable);
}
