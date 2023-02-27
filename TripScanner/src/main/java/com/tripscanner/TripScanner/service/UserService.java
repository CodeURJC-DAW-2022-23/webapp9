package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

import com.tripscanner.TripScanner.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tripscanner.TripScanner.model.User;
import com.tripscanner.TripScanner.repository.UserRepository;

@Service
public class UserService implements AbstractService<User> {

    @Autowired
    private UserRepository repository;

    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean exist(long id) {
        return repository.existsById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    public void save(User user) {
        repository.save(user);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

}
