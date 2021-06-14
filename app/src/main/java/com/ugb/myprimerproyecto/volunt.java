package com.ugb.myprimerproyecto;

public class volunt {
    String idpo;
    String rev;
    String nombrepo;
    String duipo ;
    String pdonar;
    String otros;
    String urlfoto;
    String urlvideo;

    public volunt(String idpo, String rev, String nombrepo, String duipo, String pdonar, String otros, String urlfoto, String urlvideo) {
        this.idpo = idpo;
        this.rev = rev;
        this.nombrepo = nombrepo;
        this.duipo = duipo;
        this.pdonar = pdonar;
        this.otros = otros;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdpo() {
        return idpo;
    }

    public void setIdpo(String idpostulado) {
        this.idpo = idpo;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombrevolunt() {
        return nombrepo;
    }

    public void setNombrepo(String nombrepo) {
        this.nombrepo = nombrepo;
    }

    public String getDuipo() {
        return duipo;
    }

    public void setDuipo(String duipo) {
        this.duipo = duipo;
    }

    public String getPdonar() {
        return pdonar;
    }

    public void setPdonar(String pdonar) {
        this.pdonar = pdonar;
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
