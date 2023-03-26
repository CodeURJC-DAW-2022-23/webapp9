package com.tripscanner.TripScanner.repository;

import java.util.Optional;

import com.tripscanner.TripScanner.model.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Destination;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface DestinationRepository extends PagingAndSortingRepository<Destination, Long>, JpaRepository<Destination, Long> {

    Optional<Destination> findByName(String name);

    @Query("SELECT d from Destination d WHERE LOWER(d.name) LIKE %:name% OR LOWER(d.description) LIKE %:description%")
    Page<Destination> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable);


    Page<Destination> findAll(Pageable pageable);

    @Query("SELECT d FROM Destination d JOIN d.places p WHERE p.id = :id")
    Page<Destination> findFromPlace(long id, Pageable pageable);

}
