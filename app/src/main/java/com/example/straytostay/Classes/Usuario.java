package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Usuario {
    private int adminId;

    // USUARIO (0)
    private String name, phone, citizenId, address, email, uid, imageUrl;

    // SHELTER (1)

    private String mision;

    // VETS (1)

    private String nit;
    private ArrayList<String> services;
    private ArrayList<String> products;


    public Usuario() {
        // Required empty constructor for Firestore
    }
    public Usuario(String name, String phone, String citizenId, String address, String email, int adminId, String uid, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.citizenId = citizenId;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.imageUrl = imageUrl;
    }

    public Usuario(String name, String phone, String address, String email, int adminId, String uid, String mision, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.mision = mision;
        this.imageUrl = imageUrl;
    }

    public Usuario(String name, String phone, String address, String email, int adminId, String uid, String nit, ArrayList<String> services, ArrayList<String> products, String imageUrl) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.nit = nit;
        this.services = services;
        this.products = products;
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMision() {
        return mision;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
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

    public void setMision(String mision) {
        this.mision = mision;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // Getters and Setters
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
