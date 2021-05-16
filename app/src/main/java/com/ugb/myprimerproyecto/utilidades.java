package com.ugb.myprimerproyecto;

public class utilidades {
    static String ip ="192.168.52.131";
         static  String urlmostrarpostulados = "http://"+ip+":5984/db_postulados/_design/postulados/_view/mis-postulados";
         static  String urlobtenerdatosvotantes = "http://"+ip+":5984/db_votantes/_design/votantes/_view/mis-votantes";
         static  String urlagregrdatosvotantes = "http://"+ip+":5984/db_votantes/";
         static  String urlagregarpostulados =   "http://"+ip+":5984/db_postulados/";
}
