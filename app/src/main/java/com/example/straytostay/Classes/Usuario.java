package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Usuario {
    private int adminId;

    // USUARIO (0)
    private String name;
    private String imageUri;
    private String phone;
    private String citizenId;
    private String address;
    private String email;
    private String uid;

    // SHELTER (1)

    private String mision;

    // VETS (1)

    private String nit;
    private ArrayList<String> services;
    private ArrayList<String> products;


    public Usuario() {
        // Required empty constructor for Firestore
    }
    public Usuario(String name, String phone, String citizenId, String address, String email, int adminId, String uid, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.citizenId = citizenId;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
    }

    public Usuario(String name, String phone, String address, String email, int adminId, String uid, String mision, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.mision = mision;
    }

    public Usuario(String name, String phone, String address, String email, int adminId, String uid, String nit, ArrayList<String> services, ArrayList<String> products, String imageUri) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.adminId = adminId;
        this.uid = uid;
        this.nit = nit;
        this.services = services;
        this.products = products;
    }

    public String getUid() {
        return uid;
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
