package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);

    @Query("SELECT p from Place p WHERE LOWER(p.name) LIKE %:name% OR LOWER(p.description) LIKE %:description%")
    Page<Place> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);


    Page<Place> findAll(Pageable pageable);

}
