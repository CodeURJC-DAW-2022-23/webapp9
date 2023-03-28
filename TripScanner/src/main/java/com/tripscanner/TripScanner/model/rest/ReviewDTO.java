package com.tripscanner.TripScanner.model.rest;

import com.tripscanner.TripScanner.model.Itinerary;
import com.tripscanner.TripScanner.model.Review;

import java.util.Date;

public class ReviewDTO {
    private String title;

    private String description;

    private int score;

    private String user;

    private String date;

    public ReviewDTO() {}

    public ReviewDTO(String title, String description, int score, String user, String date) {
        super();
        this.title = title;
        this.description = description;
        this.score = score;
        this.user = user;
        this.date = date;
    }

    public ReviewDTO(Review review) {
        super();
        this.title = review.getTitle();
        this.description = review.getDescription();;
        this.score = review.getScore();
        this.user = review.getUser().getUsername();
        this.date = review.getDateToString();
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
