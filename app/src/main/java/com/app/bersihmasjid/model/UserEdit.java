package com.app.bersihmasjid.model;

public class UserEdit {
        private String point;
    private String account;
    private String description;

    public UserEdit() {

    }

    public UserEdit(String point, String account, String description) {

        this.point = point;
        this.account = account;
        this.description = description;
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
