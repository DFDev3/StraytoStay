package com.example.straytostay.Classes;

public class Recurso {

    private String rid, tipo, titulo, link, imagenUrl;

    public Recurso(String rid, String tipo, String titulo, String link, String imagenUrl) {
        this.rid = rid;
        this.tipo = tipo;
        this.titulo = titulo;
        this.link = link;
        this.imagenUrl = imagenUrl;
    }

    public Recurso() {
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
