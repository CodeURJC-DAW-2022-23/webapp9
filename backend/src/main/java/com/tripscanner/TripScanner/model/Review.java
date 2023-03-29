package com.tripscanner.TripScanner.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long views;

    private int score;

    private Date date;

    @ManyToOne
    private Itinerary itinerary;

    @ManyToOne
    private User user;

    public Review() {
    }

    public Review(String title, String description, int score) {
        super();
        this.title = title;
        this.description = description;
        this.score = score;
        this.date = new Date();
        this.setViews(0L);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public String getDateToString() {
        return new SimpleDateFormat("dd/MM/yyyy, HH:mm").format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
