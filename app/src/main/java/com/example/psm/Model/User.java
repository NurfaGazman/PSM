package com.example.psm.Model;

import java.util.Date;

public class User {

    private int user_Id;
    private String email;
    private String full_name;
    private String password;
    private String address;
    private String blood_gp;
    private Date birth_date;
    private String medical_id;

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlood_gp() {
        return blood_gp;
    }

    public void setBlood_gp(String blood_gp) {
        this.blood_gp = blood_gp;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }

    public String getMedical_id() {
        return medical_id;
    }

    public void setMedical_id(String medical_id) {
        this.medical_id = medical_id;
    }

//contractor USER
    public User(){
        user_Id = -1;
        email = "";
        full_name = "";
        password = "";
        blood_gp = "";
        address = "";
        birth_date = new Date();
        medical_id = "";
    }
}
