package com.example.straytostay.Classes;

public class Shelter {

    private String uid,imageUrl,name,phone,address,email,mission,website;
    private int adminId;

    public Shelter(){}

    public Shelter(String uid, int adminId, String imageUrl,String name, String phone, String address, String email, String mision,String website) {
        this.uid = uid;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phone = phone;
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
