package com.ugb.myprimerproyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorImagenes extends BaseAdapter {
    Context context;
    ArrayList<MainActivity.productos> productosArrayList;
    LayoutInflater layoutInflater;
    MainActivity.productos misproductos;

    public adaptadorImagenes(Context context, ArrayList<MainActivity.productos> productosArrayList) {
        this.context = context;
        this.productosArrayList = productosArrayList;
    }

    @Override
    public int getCount() {
        return  productosArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return productosArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  Long.parseLong( productosArrayList.get(position).getIdproducto() );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View encuadre = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView temp = encuadre.findViewById(R.id.lblnombre);
        ImageView img = encuadre.findViewById(R.id.miniatura);
        try{
            misproductos = productosArrayList.get(position);
            temp.setText(misproductos.getDescripcion());
            Bitmap imagenBitmap = BitmapFactory.decodeFile(misproductos.getUrlfoto());
            img.setImageBitmap(imagenBitmap);
        }catch (Exception e){
        }
        return encuadre;
    }
}

