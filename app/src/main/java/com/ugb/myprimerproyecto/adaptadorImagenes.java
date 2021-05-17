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
    ArrayList<com.ugb.myprimerproyecto.voluntarios> datosvoluntariosArrayList;
    LayoutInflater layoutInflater;
    com.ugb.myprimerproyecto.voluntarios voluntarios;

    public adaptadorImagenes(Context context, ArrayList<com.ugb.myprimerproyecto.voluntarios> datospostuladosArrayList) {
        this.context = context;
        this.datosvoluntariosArrayList = datospostuladosArrayList;
    }

    @Override
    public int getCount() {
        return datosvoluntariosArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datosvoluntariosArrayList.get(position);
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
            voluntarios = datosvoluntariosArrayList.get(position);
            temp.setText(voluntarios.getNombrepostulado());

          String urldefoto = voluntarios.getUrlfoto();

         img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}

