package com.tripscanner.TripScanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tripscanner.TripScanner.model.rest.UserDTO;

import javax.persistence.*;
import java.sql.Blob;
import java.util.List;

@Entity
@Table(name = "UserTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String passwordHash;

    private String nationality;

    @Lob
    @JsonIgnore
    private Blob imageFile;

    private boolean image;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Itinerary> itineraries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    public User() {
    }

    public User(UserDTO userDTO) {
        super();
        this.username = userDTO.getUsername();
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.email = userDTO.getEmail();
        this.passwordHash = userDTO.getPasswordHash();
        this.nationality = userDTO.getNationality();
        this.roles = List.of("USER");
    }

    public User(String username, String firstName, String lastName, String email, String passwordHash, String nationality, String... roles) {
        super();
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.nationality = nationality;
        this.roles = List.of(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean hasUserName() {
        return !this.username.isEmpty() || !this.username.isBlank();
    }

    public boolean hasFirstName() {
        return !this.firstName.isEmpty() || !this.firstName.isBlank();
    }

    public boolean hasLastName() {
        return !this.lastName.isEmpty() || !this.lastName.isBlank();
    }

    public boolean hasEmail() {
        return !this.email.isEmpty() || !this.email.isBlank();
    }

    public boolean hasPasswordHash() {
        return !this.passwordHash.isEmpty() || !this.passwordHash.isBlank();
    }
}