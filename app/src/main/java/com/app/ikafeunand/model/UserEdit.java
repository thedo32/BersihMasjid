package com.app.ikafeunand.model;

public class UserEdit {
    private String bname;
    private String jalamat;
    private String fpekerjaan;
    private String gmobile;

    public UserEdit() {

    }

    public UserEdit(String bname, String jalamat, String fpekerjaan, String gmobile) {

        this.bname = bname;
        this.jalamat = jalamat;
        this.fpekerjaan = fpekerjaan;
        this.gmobile = gmobile;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getJalamat() {
        return jalamat;
    }

    public void setJalamat(String jalamat) {
        this.jalamat = jalamat;
    }

    public String getFpekerjaan() {
        return fpekerjaan;
    }

    public void setFpekerjaan(String fpekerjaan) {
        this.fpekerjaan = fpekerjaan;
    }

    public String getGmobile() {
        return gmobile;
    }

    public void setGmobile(String gmobile) {
        this.gmobile = gmobile;
    }
}
