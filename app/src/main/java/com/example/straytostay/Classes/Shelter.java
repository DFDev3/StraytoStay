package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Shelter {

    private String uid,imageUrl,name,address,email,mission,website;
    private ArrayList<String> phoneList;
    private int adminId;

    public Shelter(){}

    public Shelter(String uid, int adminId, String imageUrl,String name, ArrayList<String> phoneList, String address, String email, String mision,String website) {
        this.uid = uid;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phoneList = phoneList;
        this.address = address;
        this.email = email;
        this.mission = mision;
        this.website = website;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<String> phoneList) {
        this.phoneList = phoneList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
