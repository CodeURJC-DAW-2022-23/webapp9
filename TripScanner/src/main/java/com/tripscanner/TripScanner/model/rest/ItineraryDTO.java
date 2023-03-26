package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Place;

import java.util.List;

public class ItineraryDTO {

    private String name;

    private String description;

    private String user;

    private List<Place> places;

    private boolean isPublic;


    public ItineraryDTO() {}

    public ItineraryDTO(String name, String description, String user) {
        super();
        this.name = name;
        this.description = description;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPlace(List<Place>places) {
        this.places = places;
    }

    public List<Place> getPlace() {
        return places;
    }

    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        this.isPublic = aPublic;
    }

    public boolean hasName() {
        return !this.name.isBlank() || !this.name.isEmpty() || !(this.name == null);
    }

    public boolean hasDescription() {
        return !(this.description == null);
    }
}


