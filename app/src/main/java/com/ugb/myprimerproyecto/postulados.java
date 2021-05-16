package com.ugb.myprimerproyecto;

public class postulados {
    String idpostulado;
    String rev;
    String nombrepostulado;
    String duipostulado ;
    String propuesta;
    String otros;
    String urlfoto;
    String urlvideo;

    public postulados(String idpostulado, String rev, String nombrepostulado, String duipostulado, String propuesta, String otros, String urlfoto, String urlvideo) {
        this.idpostulado = idpostulado;
        this.rev = rev;
        this.nombrepostulado = nombrepostulado;
        this.duipostulado = duipostulado;
        this.propuesta = propuesta;
        this.otros = otros;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdpostulado() {
        return idpostulado;
    }

    public void setIdpostulado(String idpostulado) {
        this.idpostulado = idpostulado;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombrepostulado() {
        return nombrepostulado;
    }

    public void setNombrepostulado(String nombrepostulado) {
        this.nombrepostulado = nombrepostulado;
    }

    public String getDuipostulado() {
        return duipostulado;
    }

    public void setDuipostulado(String duipostulado) {
        this.duipostulado = duipostulado;
    }

    public String getPropuesta() {
        return propuesta;
    }

    public void setPropuesta(String propuesta) {
        this.propuesta = propuesta;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
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
