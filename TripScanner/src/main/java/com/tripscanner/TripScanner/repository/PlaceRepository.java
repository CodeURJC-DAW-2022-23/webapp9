package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);

    List<Optional<Destination>> findByNameOrDescriptionLikeIgnoreCase(String query);

}
