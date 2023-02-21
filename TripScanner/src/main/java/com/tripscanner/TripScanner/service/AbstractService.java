package com.tripscanner.TripScanner.service;

import java.util.List;
import java.util.Optional;

public interface AbstractService<D> {

    public Optional<D> findById(long id);

    public boolean exist(long id);

    public List<D> findAll();

    public void save(D data);

    public void delete(long id);

}
