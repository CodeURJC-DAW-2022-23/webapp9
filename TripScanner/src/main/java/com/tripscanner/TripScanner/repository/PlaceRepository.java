package com.tripscanner.TripScanner.repository;


import java.util.Optional;

import com.tripscanner.TripScanner.model.Destination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);

    @Query("SELECT p from Place p WHERE LOWER(p.name) LIKE %:name% OR LOWER(p.description) LIKE %:description%")
    Page<Place> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);

   /* @Modifying
    @Query("DELETE d FROM Place WHERE d.destination_id = :id")
    Optional<Place> deleteFromPlace(long id);*/

    Page<Place> findAll(Pageable pageable);

}
