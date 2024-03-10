package com.example.habittracker.models;

public class RatingModel {

    private String email;
    private float stars;
    private String opinion;

    public RatingModel(String email, float stars, String opinion) {
        this.email = email;
        this.stars = stars;
        this.opinion = opinion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float starts) {
        this.stars = starts;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
