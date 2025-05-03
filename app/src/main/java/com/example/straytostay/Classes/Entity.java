package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Entity {
    private String uid,imageUrl,nit,name,address,email,mision,website;
    private int adminId, verified;
    private ArrayList<String> services, products, phoneList;

    public Entity(){}

    public Entity(int verified, String uid, int adminId, String imageUrl, String name, ArrayList<String> phoneList, String address, String email, String mision, String website) {
        this.verified = verified;
        this.uid = uid;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.phoneList = phoneList;
        this.address = address;
        this.email = email;
        this.mision = mision;
        this.website = website;
    }

    public Entity(int verified, String uid, int adminId, String imageUrl, String nit, String name, ArrayList<String> phoneList, String address, String email, ArrayList<String> services, ArrayList<String> products, String website) {
        this.verified = verified;
        this.uid = uid;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
        this.nit = nit;
        this.name = name;
        this.phoneList = phoneList;
        this.address = address;
        this.email = email;
        this.services = services;
        this.products = products;
        this.website = website;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(ArrayList<String> phoneList) {
        this.phoneList = phoneList;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public ArrayList<String> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<String> products) {
        this.products = products;
    }

    public String getMision() {
        return mision;
    }

    public void setMision(String mision) {
        this.mision = mision;
    }
}
