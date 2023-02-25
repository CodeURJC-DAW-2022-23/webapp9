package com.tripscanner.TripScanner.model;

import java.sql.Blob;

public interface Information {

    Long getId();

    String getName();

    String getDescription();

    String getType();

    String getFlag();

}
