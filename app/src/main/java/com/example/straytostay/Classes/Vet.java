package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Vet {
    private String uid,imageUrl,nit,name,phone,address,email,website;
    private int adminId;
    private ArrayList<String> services, products;

    public Vet(){}

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

    public Vet(String uid, int adminId, String imageUrl, String nit, String name, String phone, String address, String email, ArrayList<String> services, ArrayList<String> products, String website) {
        this.uid = uid;
        this.adminId = adminId;
        this.imageUrl = imageUrl;
        this.nit = nit;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.services = services;
        this.products = products;
        this.website = website;
    }
}
