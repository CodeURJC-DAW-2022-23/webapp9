package com.tripscanner.TripScanner.repository;

import com.tripscanner.TripScanner.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAll(Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.itinerary.id = :id")
    Page<Review> findFromItinerary(long id, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.user.id = :id")
    Page<Review> findFromUser(long id, Pageable pageable);

}
