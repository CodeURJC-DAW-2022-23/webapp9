package com.tripscanner.TripScanner.service;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Itinerary> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Page<Itinerary> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Itinerary> findAllPublic(Pageable pageable) { return repository.findAllPublic(pageable); }

    public void save(Itinerary itinerary) {
        repository.save(itinerary);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Page<Itinerary> findAllByNameOrDescriptionLikeIgnoreCase(String name, String description, Pageable pageable) {
        return repository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable);
    }

    public Page<Itinerary> findAllByUserOrPublic(String user, Pageable pageable) {
        return repository.findAllByUserOrPublic(user, pageable); 
    }

    public Page<Itinerary> findFromPlace(long id, String user, Pageable pageable) {
        return repository.findFromPlace(id, user, pageable);
    }

    public Page<Itinerary> findFromUser(long id, Pageable pageable) {
        return repository.findFromUser(id, pageable);
    }
    
    public Page<Itinerary> findAllByNameOrDescriptionAndUserOrPublicLikeIgnoreCase(String name, String description, String user, Pageable pageable) {
        return repository.findAllByNameOrDescriptionAndUserOrPublicLikeIgnoreCase(name, description, user, pageable);
    }

    public Page<Itinerary> findAllByNameOrDescriptionLikeIgnoreCasePublic(String name, String description, Pageable pageable) {
        return repository.findAllByNameOrDescriptionLikeIgnoreCasePublic(name, description, pageable);
    }

    public Page<Itinerary> findAllByUsername(String username, Pageable pageable) { return repository.findAllByUsername(username, pageable); }
}
