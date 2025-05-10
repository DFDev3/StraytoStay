package com.example.straytostay.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mascota implements Serializable {
    private String aid, nombre, edad, raza, tipo, esterilizacion, sexo, tamano, descripcion, estado, imageUrl, refugio;
    private ArrayList<String> vacunas, appliedBy;

    // Required empty constructor for Firestore
    public Mascota() {}

    // Full constructor
    public Mascota(String aid, String nombre, String edad, String raza, String tipo, String esterilizacion,
                   String sexo, ArrayList<String> vacunas, String tamano, String descripcion, String estado, String refugio, String imageUrl) {
        this.aid = aid;
        this.nombre = nombre;
        this.edad = edad;
        this.raza = raza;
        this.tipo = tipo;
        this.esterilizacion = esterilizacion;
        this.sexo = sexo;
        this.vacunas = vacunas;
        this.tamano = tamano;
        this.descripcion = descripcion;
        this.estado = estado;
        this.imageUrl = imageUrl;
        this.refugio = refugio;
    }

    // Getters and Setters

    public ArrayList<String> getAppliedBy() {
        return appliedBy;
    }

    public void setAppliedBy(ArrayList<String> appliedBy) {
        this.appliedBy = appliedBy;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAid() { return aid; }

    public String getRefugio() {
        return refugio;
    }

    public void setRefugio(String refugio) {
        this.refugio = refugio;
    }

    public void setAid(String aid) { this.aid = aid; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEsterilizacion() { return esterilizacion; }
    public void setEsterilizacion(String esterilizacion) { this.esterilizacion = esterilizacion; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public ArrayList<String> getVacunas() {
        return vacunas;
    }

    public void setVacunas(ArrayList<String> vacunas) {
        this.vacunas = vacunas;
    }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
