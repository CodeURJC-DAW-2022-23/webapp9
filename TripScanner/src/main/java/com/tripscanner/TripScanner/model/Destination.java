package com.tripscanner.TripScanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Blob;
import java.util.List;

import javax.persistence.*;

@Entity
public class Destination implements Information {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long views;

    @Lob
    @JsonIgnore
    private Blob imageFile;

    private boolean image;

    private String flagCode;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Place> places;

    public Destination() {
    }

    public Destination(String name, String description, String flagCode) {
        super();
        this.name = name;
        this.description = description;
        this.flagCode = flagCode.toLowerCase();
        this.setViews(0L);
        this.setImage(false);
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

    @Override
    public String getType() {
        return "Destination";
    }

    //public String getSearchResult(){ return "Destination";}

    @Override
    public String getTypeLowercase() {
        return getType().toLowerCase();
    }

    @Override
    public String getFlag() {
        return "https://flagicons.lipis.dev/flags/4x3/" + getFlagCode() + ".svg";
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

    public String getFlagCode() {
        return flagCode;
    }

    public void setFlagCode(String flagCode) {
        this.flagCode = flagCode;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }


}
