package com.ugb.myprimerproyecto;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<postulados> datospostuladosArrayList;
    LayoutInflater layoutInflater;
    postulados mispostulados;

    public adaptadorImagenes(Context context, ArrayList<postulados> datospostuladosArrayList) {
        this.context = context;
        this.datospostuladosArrayList = datospostuladosArrayList;
    }

    @Override
    public int getCount() {
        return datospostuladosArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datospostuladosArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View encuadre = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView temp = encuadre.findViewById(R.id.lbltitulo);
        ImageView img = encuadre.findViewById(R.id.miniatura);
        try{
            mispostulados = datospostuladosArrayList.get(position);
            temp.setText(mispostulados.getNombrepostulado());

            temp = encuadre.findViewById(R.id.lblotros);
            temp.setText(mispostulados.getOtros());

          String urldefoto = mispostulados.getUrlfoto();

         img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}

