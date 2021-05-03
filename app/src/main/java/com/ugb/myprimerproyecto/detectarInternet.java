package com.ugb.myprimerproyecto;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//Diego Alexander Alemán Castro        USIS012120
//Mauricio Adonay Espinoza Villatoro   USIS018120
//Mariano José Ortega Hernández        USIS016820
//Kevin Aníbal Campos jurado           USIS017320
public class detectarInternet {

    private Context context;

    public detectarInternet(Context context) {
        this.context = context;
    }
    public boolean hayConexionInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if( connectivityManager!=null ){
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if( networkInfos!=null ){
                for (int i=0; i<networkInfos.length; i++){
                    if( networkInfos[i].getState()==NetworkInfo.State.CONNECTED ){
                        return  true;
                    }
                }
            }
        }
        return false;
    }
}
