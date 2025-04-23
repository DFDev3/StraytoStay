package com.example.straytostay.Classes;

import java.util.ArrayList;

public class Vet {
    private String uid,imageUrl,nit,name,phone,address,email;
    private int adminId;
    private ArrayList<String> services, products;

    public Vet(){}

    public Vet(String uid,  int adminId, String imageUrl, String nit, String name, String phone, String address, String email, ArrayList<String> services, ArrayList<String> products) {
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
    }
}
