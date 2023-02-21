package com.tripscanner.TripScanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tripscanner.TripScanner.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
