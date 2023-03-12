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

@Service
public class SearchService implements AbstractService<Information> {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;



    public List<Information> searchInfo(String name, String description) {
        List<Information> result = new ArrayList<>();
        result.addAll(destinationRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description));
        result.addAll(placeRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description));
        result.addAll(itineraryRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description));
        return result;
    }

    public List<Information> searchInfoSort(String name, String description, Pageable pageable) {
        List<Information> result = new ArrayList<>();
        result.addAll(destinationRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        result.addAll(placeRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        //result.addAll(itineraryRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        return result;
    }

    public List<Information> searchInfoDestPlace(String name, String description, Pageable pageable) {
        List<Information> result = new ArrayList<>();
        result.addAll(destinationRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        result.addAll(placeRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        return result;
    }

    public List<Information> searchInfoDestItinerary(String name, String description, Pageable pageable) {
        List<Information> result = new ArrayList<>();
        result.addAll(destinationRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        //result.addAll(itineraryRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        return result;
    }

    public List<Information> searchInfoPlaceItinerary(String name, String description, Pageable pageable) {
        List<Information> result = new ArrayList<>();
        result.addAll(placeRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        //result.addAll(itineraryRepository.findAllByNameOrDescriptionContainingIgnoreCase(name, description, pageable));
        return result;
    }
    public List<Information> orderByName(String name, String description, Pageable pageable) {
        List<Information> result = new ArrayList<>();
        result.addAll(destinationRepository.findAllByNameOrDescriptionOrderByName(name, description, pageable));
        result.addAll(placeRepository.findAllByNameOrDescriptionOrderByName(name, description, pageable));
        result.addAll(itineraryRepository.findAllByNameOrDescriptionOrderByName(name, description, pageable));
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