package com.tripscanner.TripScanner.model.rest;

public class ItineraryDTO {

    private String name;

    private String description;

    private boolean publicValue;

    private String user;

    public ItineraryDTO() {}

    public ItineraryDTO(String name, String description, String user) {
        super();
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public ItineraryDTO(String name, String description, boolean publicValue) {
        this.name = name;
        this.description = description;
        this.publicValue = publicValue;
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
        return publicValue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPublicValue(boolean publicValue) {
        this.publicValue = publicValue;
    }

    public boolean hasName() {
        return !this.name.isBlank() || !this.name.isEmpty() || !(this.name == null);
    }

    public boolean hasDescription() {
        return !(this.description == null);
    }
}
