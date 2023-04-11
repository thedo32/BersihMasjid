package com.app.bersihmasjid.model;

public class UserDiary {
    private String name;
    private String email;
    private String mobile;
    private String point;
    private String addpoint;
    private String account;
    private String description;

    public UserDiary() {

    }

    public String getAddpoint() {
        return addpoint;
    }

    public void setAddpoint(String addpoint) {
        this.addpoint = addpoint;
    }

    public UserDiary(String name, String email, String mobile, String point, String addpoint, String account, String description) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.point = point;
        this.addpoint = addpoint;
        this.account = account;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
