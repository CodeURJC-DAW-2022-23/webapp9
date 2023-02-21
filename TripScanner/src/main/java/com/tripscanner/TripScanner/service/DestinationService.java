package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<Destination> findAll() {
        return repository.findAll();
    }

    public void save(Destination destination) {
        repository.save(destination);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
