package com.raivogaming.vegescanner;

public class User {

    String username, imagepath;
    boolean premium;

    public User(){

    }

    public User(String username, String imagepath, boolean premium){
        this.username = username;
        this.imagepath = imagepath;
        this.premium = premium;

    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
