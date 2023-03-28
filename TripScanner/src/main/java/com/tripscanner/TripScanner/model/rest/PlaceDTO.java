package com.tripscanner.TripScanner.model.rest;

public class PlaceDTO {

    private String name;

    private String description;

    private String destination;

    public PlaceDTO(){
    }

    public PlaceDTO(String name, String description, String destination) {
        this.name = name;
        this.description = description;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
