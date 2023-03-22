package com.tripscanner.TripScanner.model.rest;

public class ItineraryDTO {

    private String name;

    private String description;

    private String user;

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

}
