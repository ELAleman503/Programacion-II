package com.ugb.myprimerproyecto;

public class voluntarios {
    String idvolunt;
    String rev;
    String nombrevolunt;
    String duivolunt ;
    String donacion;
    String urlfoto;
    String urlvideo;

    public voluntarios(String idvolunt, String rev, String nombrevolunt, String duivolunt, String donacion,  String urlfoto, String urlvideo) {
        this.idvolunt = idvolunt;
        this.rev = rev;
        this.nombrevolunt = nombrevolunt;
        this.duivolunt = duivolunt;
        this.donacion = donacion;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdpostulado() {
        return idvolunt;
    }

    public void setIdvolunt(String idvolunt) {
        this.idvolunt = idvolunt;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombrevolunt() {
        return nombrevolunt;
    }

    public void setNombrevolunt(String nombrevolunt) {
        this.nombrevolunt = nombrevolunt;
    }

    public String getDuipostulado() {
        return duivolunt;
    }

    public void setDuivolunt(String duivolunt) {
        this.duivolunt = duivolunt;
    }

    public String getDonacion() {
        return donacion;
    }

    public void setDonacion(String donacion) {
        this.donacion = donacion;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getUrlvideo() {
        return urlvideo;
    }

    public void setUrlvideo(String urlvideo) {
        this.urlvideo = urlvideo;
    }
}
