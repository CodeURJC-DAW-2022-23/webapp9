package com.tripscanner.TripScanner.service;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface AbstractService<D> {

    public Optional<D> findById(long id);

    public boolean exist(long id);

    public List<D> findAll();

    public List<D> findAll(Sort sort);

    public void save(D data);

    public void delete(long id);

}
