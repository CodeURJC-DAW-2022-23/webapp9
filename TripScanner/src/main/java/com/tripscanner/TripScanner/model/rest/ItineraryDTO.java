package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Place;
import java.util.List;

public class ItineraryDTO {

    private String name;

    private String description;

    private boolean isPublic;

    private String user;

    public ItineraryDTO() {}

    public ItineraryDTO(String name, String description, String user) {
        super();
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public ItineraryDTO(String name, String description, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
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

    public boolean isPublic() {
        return isPublic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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
