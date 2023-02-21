package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.repository.ItineraryRepository;

@Service
public class ItineraryService implements AbstractService<Itinerary> {

    @Autowired
    private ItineraryRepository repository;

    public Optional<Itinerary> findById(long id) {
        return repository.findById(id);
    }

    public Optional<Itinerary> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Itinerary> findAll() {
        return repository.findAll();
    }

    public void save(Itinerary itinerary) {
        repository.save(itinerary);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
