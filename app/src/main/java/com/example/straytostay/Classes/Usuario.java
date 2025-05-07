package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Usuario {
    private int adminId, score;
    private String name, phone, citizenId, address, email, uid, imageUrl;


    public Usuario() {
        // Required empty constructor for Firestore
    }

    public Usuario(String name, String phone, String citizenId, String address, String email, int adminId, String uid, String imageUrl, int score) {
        this.name = name;
        this.phone = phone;
        this.citizenId = citizenId;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.score = score;
    }


    public Usuario(String uid, String name, String phone, String email, String imageUrl, int adminId){
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}