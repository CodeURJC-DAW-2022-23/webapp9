package com.tripscanner.TripScanner.model;

import java.util.List;

import javax.persistence.*;

@Entity
public class Itinerary implements Information {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long views;

    @ManyToMany
    private List<Place> places;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy="itinerary", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Review> reviews;

    public Itinerary() {
    }

    public Itinerary(String name, String description) {
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

    @Override
    public String getType() {
        return "Itinerary";
    }

    @Override
    public String getTypeLowercase() {
        return getType().toLowerCase();
    }

    @Override
    public String getFlag() {
        if (places.isEmpty()) {
            return "https://flagicons.lipis.dev/flags/4x3/xx.svg";
        } else {
            return places.get(0).getFlag();
        }
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

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

}
