package com.tripscanner.TripScanner.model;

import java.sql.Blob;

public interface Information {

    Long getId();

    String getName();

    String getDescription();

    String getType();

    String getTypeLowercase();

    boolean isImage();

    Blob getImageFile();

    String getFlag();


}
