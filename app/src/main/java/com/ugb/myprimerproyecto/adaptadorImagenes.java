package com.ugb.myprimerproyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes  extends BaseAdapter {

    Context context;
    ArrayList<pelis> datospelisArrayList;
    LayoutInflater layoutInflater;
    pelis misPelis;

    public adaptadorImagenes(Context context, ArrayList<pelis> datospelisArrayList) {
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
        ImageView img = encuadre.findViewById(R.id.miniatura);
        try{
            misPelis = datospelisArrayList.get(position);
            temp.setText(misPelis.getTitulo());

            temp = encuadre.findViewById(R.id.lblprecio);
            temp.setText("$"+misPelis.getPrecio());

          String urldefoto = misPelis.getUrlfoto();


         img.setImageURI(Uri.parse(urldefoto));

        }catch (Exception e){
        }
        return encuadre;
    }
}

