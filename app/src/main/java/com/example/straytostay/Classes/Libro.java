package com.example.straytostay.Classes;

public class Libro {

    private String rid, decripcion,fecha,autor,pdfurl;

    public Libro() {
    }

    public Libro(String rid, String decripcion, String fecha, String autor, String pdfurl) {
        this.rid = rid;
        this.decripcion = decripcion;
        this.fecha = fecha;
        this.autor = autor;
        this.pdfurl = pdfurl;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDecripcion() {
        return decripcion;
    }

    public void setDecripcion(String decripcion) {
        this.decripcion = decripcion;
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

    public String getPdfurl() {
        return pdfurl;
    }

    public void setPdfurl(String pdfurl) {
        this.pdfurl = pdfurl;
    }
}
