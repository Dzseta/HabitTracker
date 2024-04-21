package com.example.habittracker.models;

public class RatingModel {

    private String uid;
    private float stars;
    private String opinion;

    public RatingModel(String uid, float stars, String opinion) {
        this.uid = uid;
        this.stars = stars;
        this.opinion = opinion;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String email) {
        this.uid = email;
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
