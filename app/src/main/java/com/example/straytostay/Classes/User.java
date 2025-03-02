package com.example.straytostay.models;

public class User {
    private String name;
    private String lastName;
    private String phone;
    private String citizenId;
    private String neighborhood;
    private String email;
    private int adminId;

    public User() {
        // Required empty constructor for Firestore
    }

    public User(String name, String lastName, String phone, String citizenId, String neighborhood, String email, int adminId) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.citizenId = citizenId;
        this.neighborhood = neighborhood;
        this.email = email;
        this.adminId = adminId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
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
