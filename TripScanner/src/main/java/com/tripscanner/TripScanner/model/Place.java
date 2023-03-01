package com.tripscanner.TripScanner.model;

import java.sql.Blob;
import java.util.List;

import javax.persistence.*;

@Entity
public class Place implements Information {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long views;

    @Lob
    private Blob imageFile;

    private boolean image;

    @ManyToOne
    private Destination destination;

    @ManyToMany(mappedBy="places")
    private List<Itinerary> itineraries;

    public Place() {
        super();
    }

    public Place(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    @Override
    public String getFlag() {
        return destination.getFlag();
    }

    public String getType() {
        return "Place";
    }

    @Override
    public String getTypeLowercase() {
        return getType().toLowerCase();
    }
}
