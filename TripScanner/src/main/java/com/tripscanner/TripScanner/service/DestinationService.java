package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tripscanner.TripScanner.model.Destination;
import com.tripscanner.TripScanner.repository.DestinationRepository;

@Service
public class DestinationService implements AbstractService<Destination> {

    @Autowired
    private DestinationRepository repository;

    public Optional<Destination> findById(long id) {
        return repository.findById(id);
    }

    public Optional<Destination> findByName(String name) {
        return repository.findByName(name);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Destination> findAll() {
        return repository.findAll();
    }

    public List<Destination> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public Page<Destination> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public void save(Destination destination) {
        repository.save(destination);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public List<Destination> findByQuery(String name, String description) {
        return repository.findAllByNameOrDescriptionLikeIgnoreCase(name, description);
    }

    public List<Destination> findByQuery(String name, String description, Pageable pageable) {
        return repository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable);
    }

}
