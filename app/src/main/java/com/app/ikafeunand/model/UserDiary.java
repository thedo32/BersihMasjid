package com.app.ikafeunand.model;

public class UserDiary {
    private String bname;
    private String hemail;
    private String gmobile;

    private String apoint;
    private String djurusan;
    private String eangkatan;
    private String cgender;
    private String jalamat;
    private String fpekerjaan;



    public UserDiary() {

    }

    public UserDiary(String bname, String hemail, String gmobile, String apoint, String  djurusan,  String eangkatan, String cgender, String jalamat, String fpekerjaan) {
        this.bname = bname;
        this.hemail = hemail;
        this.gmobile = gmobile;
        this.apoint = apoint;
        this.djurusan = djurusan;
        this.eangkatan = eangkatan;
        this.cgender = cgender;
        this.jalamat = jalamat;
        this.fpekerjaan = fpekerjaan;

    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getHemail() {
        return hemail;
    }

    public void setHemail(String hemail) {
        this.hemail = hemail;
    }

    public String getGmobile() {
        return gmobile;
    }

    public void setGmobile(String gmobile) {
        this.gmobile = gmobile;
    }

    public String getApoint() {
        return apoint;
    }

    public void setApoint(String apoint) {
        this.apoint = apoint;
    }

    public String getDjurusan() {
        return djurusan;
    }

    public void setDjurusan(String djurusan) {
        this.djurusan = djurusan;
    }

    public String getEangkatan() {
        return eangkatan;
    }

    public void setEangkatan(String eangkatan) {
        this.eangkatan = eangkatan;
    }

    public String getCgender() {
        return cgender;
    }

    public void setCgender(String cgender) {
        this.cgender = cgender;
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
}
