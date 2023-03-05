package com.tripscanner.TripScanner.service;

import com.tripscanner.TripScanner.model.Information;
import com.tripscanner.TripScanner.repository.DestinationRepository;
import com.tripscanner.TripScanner.repository.ItineraryRepository;
import com.tripscanner.TripScanner.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SearchService implements AbstractService<Information>{

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;



    public List<Information> searchInfo(String name, String description, Pageable pageable, String type) {
        List<Information> result = new ArrayList<>();
        result.add((Information) destinationRepository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable));
        result.add((Information) placeRepository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable));
        result.add((Information) itineraryRepository.findAllByNameOrDescriptionLikeIgnoreCase(name, description, pageable));
        if (name != null) {
            result = result.stream()
                    .filter(p -> p.getType().toLowerCase().contains(type))
                    .collect(Collectors.toList());
        }
        return result;
    }


    @Override
    public Optional<Information> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean exist(long id) {
        return false;
    }

    @Override
    public List<Information> findAll() {
        return null;
    }

    @Override
    public List<Information> findAll(Sort sort) {
        return null;
    }

    @Override
    public void save(Information data) {

    }

    @Override
    public void delete(long id) {

    }
}