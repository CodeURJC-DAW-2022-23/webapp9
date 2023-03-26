package com.tripscanner.TripScanner.model.rest;

public class ItineraryDTO {

    private String name;

    private String description;

    private boolean isPublic;

    public ItineraryDTO() { }

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

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
