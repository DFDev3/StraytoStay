package com.example.straytostay.Classes;

public class Video {

    private String rid, descripcion, videourl, thumbnailurl,fecha,canal;

    public Video() {
    }

    public Video(String rid, String descripcion, String videourl, String thumbnailurl, String fecha, String canal) {
        this.rid = rid;
        this.descripcion = descripcion;
        this.videourl = videourl;
        this.thumbnailurl = thumbnailurl;
        this.fecha = fecha;
        this.canal = canal;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getThumbnailurl() {
        return thumbnailurl;
    }

    public void setThumbnailurl(String thumbnailurl) {
        this.thumbnailurl = thumbnailurl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }
}
