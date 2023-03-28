package com.tripscanner.TripScanner.repository;

import com.tripscanner.TripScanner.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.itineraries i WHERE i.id = :id")
    Page<User> findFromItinerary(long id, Pageable pageable);

}
