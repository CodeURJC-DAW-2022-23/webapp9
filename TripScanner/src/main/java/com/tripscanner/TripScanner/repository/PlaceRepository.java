package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Itinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);

    //List<Place> findAllByIdLikeIgnoreCase(long id);

    List<Place> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description);

    List<Place> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);

    List<Place> findAllByNameOrDescriptionOrderByName(String name, String description, Pageable pageable);

    Page<Itinerary> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);


    Page<Place> findAll(Pageable pageable);

}
