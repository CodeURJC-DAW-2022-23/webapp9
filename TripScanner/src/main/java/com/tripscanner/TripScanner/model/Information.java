package com.tripscanner.TripScanner.model;

import java.sql.Blob;

public class Information {

    private String name;
    private String type;
    private Blob flag;

    public Information(Blob flag, String name, String type) {
        this.name = name;
        this.type = type;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Blob getFlag() {
        return flag;
    }
}
