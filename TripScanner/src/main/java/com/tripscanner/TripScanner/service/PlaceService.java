package com.tripscanner.TripScanner.service;

import com.tripscanner.TripScanner.model.Place;
import com.tripscanner.TripScanner.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService implements AbstractService<Place> {

    @Autowired
    private PlaceRepository repository;

    public Optional<Place> findById(long id) {
        return repository.findById(id);
    }

    public Optional<Place> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Place> findAll() {
        return repository.findAll();
    }

    public List<Place> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Page<Place> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    public void save(Place place) {
        repository.save(place);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Page<Place> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable) {
        return repository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable);
    }

    public Page<Place> findFromDestination(long id, Pageable pageable) {
        return repository.findFromDestination(id, pageable);
    }

    public Page<Place> findFromItinerary(long id, Pageable pageable) {
        return repository.findFromItinerary(id, pageable);
    }
}
