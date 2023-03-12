package com.tripscanner.TripScanner.repository;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Itinerary;
import org.springframework.data.domain.Sort;
import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Destination;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface DestinationRepository extends PagingAndSortingRepository<Destination, Long>, JpaRepository<Destination, Long> {

    Optional<Destination> findByName(String name);

    //List<Destination> findAllByIdLikeIgnoreCase(long id);

    List<Destination> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description);

    List<Destination> findAllByNameOrDescriptionContainingIgnoreCase(String name, String description, Pageable pageable);
    List<Destination> findAllByNameOrDescriptionOrderByName(String name, String description, Pageable pageable);

    Page<Destination> findAll(Pageable pageable);


}
