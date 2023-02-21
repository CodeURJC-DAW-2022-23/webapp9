package com.tripscanner.TripScanner.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    Optional<Destination> findByName(String name);

}
