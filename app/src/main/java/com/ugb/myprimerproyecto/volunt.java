package com.ugb.myprimerproyecto;

public class volunt {
    String idpo;
    String rev;
    String nombrepo;
    String duipo ;
    String propuesta_donar;
    String otras;
    String urlfoto;
    String urlvideo;

    public volunt(String idpostulado, String rev, String nombrepo, String duipo, String propuesta_donar, String otras, String urlfoto, String urlvideo) {
        this.idpo = idpostulado;
        this.rev = rev;
        this.nombrepo = nombrepo;
        this.duipo = duipo;
        this.propuesta_donar = propuesta_donar;
        this.otras = otras;
        this.urlfoto = urlfoto;
        this.urlvideo = urlvideo;
    }

    public String getIdpo() {
        return idpo;
    }

    public void setIdpo(String idpo) {
        this.idpo = idpo;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNombrepos() {
        return nombrepo;
    }

    public void setNombrepo(String nombrepostulado) {
        this.nombrepo = nombrepo;
    }

    public String getDuipostulado() {
        return duipo;
    }

    public void setDuipo(String duipo) {
        this.duipo = duipo;
    }

    public String getPropuesta_donar() {
        return propuesta_donar;
    }

    public void setPropuesta_donar(String propuesta_donar) {
        this.propuesta_donar = propuesta_donar;
    }

    public String getOtros() {
        return otras;
    }

    public void setOtras(String otras) {
        this.otras = otras;
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
