package com.app.bersihmasjid.model;

public class ModelDiary {
    private String title;
    private String description;
    private String date;
    private String lat;
    private String lon;


    private String starttm;
    private String endtm;

    private String userid;

    private String updater;

    public ModelDiary() {

    }

    public ModelDiary(String title, String description, String date, String userid,
                      String lat, String lon,String startm, String endtm,String updater) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userid = userid;
        this.lat = lat;
        this.lon = lon;
        this.starttm = startm;
        this.endtm = endtm;
        this.updater = updater;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }


    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getUpdater() {
        return updater;
    }


    public String getStarttm() {return starttm;}

    public void setStarttm(String starttm) {this.starttm = starttm;}

    public String getEndtm() {return endtm;}

    public void setEndtm(String endtm) { this.endtm = endtm; }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
}
