package com.example.straytostay.Classes;

public class Revista {

    private String rid, fecha, autor, contenido, descripcion, portadaurl;

    public Revista() {
    }

    public Revista(String rid, String fecha, String autor, String contenido, String descripcion, String portadaurl) {
        this.rid = rid;
        this.fecha = fecha;
        this.autor = autor;
        this.contenido = contenido;
        this.descripcion = descripcion;
        this.portadaurl = portadaurl;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPortadaurl() {
        return portadaurl;
    }

    public void setPortadaurl(String portadaurl) {
        this.portadaurl = portadaurl;
    }
}
