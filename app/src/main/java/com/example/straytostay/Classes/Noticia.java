package com.example.straytostay.Classes;

public class Noticia {

    private String nid, titulo, descripcion, imageUrl;

    public Noticia(){}

    public Noticia(String nid, String titulo, String imageUrl) {
        this.nid = nid;
        this.titulo = titulo;
        this.descripcion = "Haz clic aquí para obtener más información acerca de esta noticia";
        this.imageUrl = imageUrl;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
