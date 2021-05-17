package com.ugb.myprimerproyecto;

public class voluntarios {
    String idvolunt;
    String rev;
    String nombrevolunt;
    String duivolunt ;
    String donacion;
    String urlfoto;
    String urlvideo;

    public voluntarios(String idpostulado, String rev, String nombrepostulado, String duipostulado, String propuesta, String otros, String urlfoto, String urlvideo) {
        this.idvolunt = idpostulado;
        this.rev = rev;
        this.nombrevolunt = nombrepostulado;
        this.duivolunt = duipostulado;
        this.donacion = propuesta;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdpostulado() {
        return idvolunt;
    }

    public void setIdpostulado(String idpostulado) {
        this.idvolunt = idpostulado;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombrepostulado() {
        return nombrevolunt;
    }

    public void setNombrepostulado(String nombrepostulado) {
        this.nombrevolunt = nombrepostulado;
    }

    public String getDuipostulado() {
        return duivolunt;
    }

    public void setDuipostulado(String duipostulado) {
        this.duivolunt = duipostulado;
    }

    public String getPropuesta() {
        return donacion;
    }

    public void setPropuesta(String propuesta) {
        this.donacion = propuesta;
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
