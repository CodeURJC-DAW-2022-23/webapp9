package com.tripscanner.TripScanner.repository;

import java.util.Optional;

import com.tripscanner.TripScanner.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import com.tripscanner.TripScanner.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.itineraries i WHERE i.id = :id")
    Page<User> findFromItinerary(long id, Pageable pageable);

}
