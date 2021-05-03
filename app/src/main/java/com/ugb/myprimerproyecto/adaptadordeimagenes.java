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

public class adaptadordeimagenes extends BaseAdapter {

    Context context;
    ArrayList<peliculas> datospelisArrayList;
    LayoutInflater layoutInflater;
    peliculas misPelis;

    public adaptadordeimagenes(Context context, ArrayList<peliculas> datospelisArrayList) {
        this.context = context;
        this.datospelisArrayList = datospelisArrayList;
    }

    @Override
    public int getCount() {
        return datospelisArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datospelisArrayList.get(position);
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
        ImageView img = encuadre.findViewById(R.id.imgfotomini);
        try{
            misPelis = datospelisArrayList.get(position);
            temp.setText(misPelis.getTitulo());

            temp = encuadre.findViewById(R.id.lblsinopsis);
            temp.setText(misPelis.getSinopsis());

          String urldefoto = misPelis.getUrlfoto();


         img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}

