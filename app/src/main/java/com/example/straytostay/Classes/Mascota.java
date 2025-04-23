package com.example.straytostay.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mascota implements Serializable {
    private String id;
    private String nombre, edad, raza, tipo, esterilizacion, sexo, tamano, descripcion, imagenUrl, refugio;
    private ArrayList<String> vacunas;

    // Required empty constructor for Firestore
    public Mascota() {}

    // Full constructor
    public Mascota(String id, String nombre, String edad, String raza, String tipo, String esterilizacion,
                   String sexo, ArrayList<String> vacunas, String tamano, String descripcion, String refugio, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.raza = raza;
        this.tipo = tipo;
        this.esterilizacion = esterilizacion;
        this.sexo = sexo;
        this.vacunas = vacunas;
        this.tamano = tamano;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.refugio = refugio;
    }

    // Getters and Setters

    public String getId() { return id; }

    public String getRefugio() {
        return refugio;
    }

    public void setRefugio(String refugio) {
        this.refugio = refugio;
    }

    public void setId(String id) { this.id = id; }

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

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
