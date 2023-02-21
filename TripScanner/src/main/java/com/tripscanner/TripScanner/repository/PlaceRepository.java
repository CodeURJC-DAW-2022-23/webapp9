package com.tripscanner.TripScanner.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findByName(String name);

}
