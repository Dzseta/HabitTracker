package com.example.habittracker.models;

public class UserModel {

    // variables
    private String uid;
    private boolean premium;

    public UserModel(String uid, boolean premium) {
        this.uid = uid;
        this.premium = premium;
    }

    public UserModel(String uid, String premium) {
        this.uid = uid;
        if(premium.equals("true")){
            this.premium = true;
        } else {
            this.premium = false;
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}
