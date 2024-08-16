package com.nate.royalquest.models;

public class Leaderboard {
    private String uid;
        private String position;
        private String img_url;
        private int points;
        private String firstname;
        private String lastname;


    public Leaderboard() {
    }

    public Leaderboard(String uid, String position, String img_url, int points, String firstname, String lastname) {
        this.uid = uid;
        this.position = position;
        this.img_url = img_url;
        this.points = points;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Leaderboard(String position, String img_url, int points, String firstname, String lastname) {
        this.position = position;
        this.img_url = img_url;
        this.points = points;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
