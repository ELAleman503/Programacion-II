package com.ugb.myprimerproyecto;


class peliculas {

    String idpeli;
    String rev;
    String titulo;
    String sinopsis;
    String duracion;
    String precio;
    String urlfoto;
    String urltriler;

    public peliculas(String idpeli, String rev, String titulo, String sinopsis, String duracion, String precio, String urlfoto, String urltriler) {
        this.idpeli = idpeli;
        this.rev = rev;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracion = duracion;
        this.precio = precio;
        this.urlfoto = urlfoto;
        this.urltriler = urltriler;
    }

    public String getIdpelis() {
        return idpeli;
    }

    public void setIdpelis(String idpelis) {
        this.idpeli = idpelis;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getUrltriler() {
        return urltriler;
    }

    public void setUrltriler(String urltriler) {
        this.urltriler = urltriler;
    }
}