package com.example.straytostay.Classes;

public class Noticia {

    private String nid, portadaurl, fecha, titulo, autor, contenido, descripcion;

    public Noticia() {
    }

    public Noticia(String nid, String portadaurl, String fecha, String descripcion, String titulo, String autor, String contenido) {
        this.nid = nid;
        this.portadaurl = portadaurl;
        this.fecha = fecha;
        this.titulo = titulo;
        this.autor = autor;
        this.contenido = contenido;
        this.descripcion = descripcion;
    }

    public String getRid() {
        return nid;
    }

    public void setRid(String rid) {
        this.nid = rid;
    }

    public String getPortadaurl() {
        return portadaurl;
    }

    public void setPortadaurl(String portadaurl) {
        this.portadaurl = portadaurl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
