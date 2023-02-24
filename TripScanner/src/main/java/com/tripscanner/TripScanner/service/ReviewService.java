package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tripscanner.TripScanner.model.Review;
import com.tripscanner.TripScanner.repository.ReviewRepository;

@Service
public class ReviewService implements AbstractService<Review> {

    @Autowired
    private ReviewRepository repository;

    public Optional<Review> findById(long id) {
        return repository.findById(id);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public void save(Review review) {
        repository.save(review);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
