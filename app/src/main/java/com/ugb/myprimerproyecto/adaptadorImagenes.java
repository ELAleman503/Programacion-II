package com.ugb.myprimerproyecto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    ArrayList<productos> datosproductosArrayList;
    LayoutInflater layoutInflater;
    productos misProductos;

    public adaptadorImagenes(Context context, ArrayList<productos> datosproductosArrayList) {
        this.context = context;
        this.datosproductosArrayList = datosproductosArrayList;
    }

    @Override
    public int getCount() {
        return datosproductosArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return datosproductosArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View encuadre = layoutInflater.inflate(R.layout.listview_imagenes, parent, false);
        TextView temp = encuadre.findViewById(R.id.lblnombre);
        ImageView img = encuadre.findViewById(R.id.miniatura);
        try{
            misProductos = datosproductosArrayList.get(position);
            temp.setText(misProductos.getDescripcion());

            temp = encuadre.findViewById(R.id.lblprecio);
            temp.setText("$"+misProductos.getPrecio());

            Bitmap imagenBitmap = BitmapFactory.decodeFile(misProductos.getUrlfoto());
            img.setImageBitmap(imagenBitmap);
        }catch (Exception e){
        }
        return encuadre;
    }
}

