package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnadd;
    DB miconexion;
    ListView Ltspeli;
    Cursor datospelicursor = null;
    ArrayList<peliculas> pelisArrayList=new ArrayList<peliculas>();
    ArrayList<peliculas> pelisArrayListCopy=new ArrayList<peliculas>();
    peliculas misPelis;
    JSONArray jsonArrayDatosPeli;
    JSONObject jsonObjectDatosPelis;
    utilidades u;
    String idlocal;
    detectarInternet di;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        di = new detectarInternet(getApplicationContext());
        btnadd = findViewById(R.id.btnagregar);
        btnadd.setOnClickListener(v->{
            Agregar("nuevo");
        });
        sincronizarDatos();
        obtenerDatos();
        Buscar();
       }

       public void sincronizarDatos() {
        if (di.hayConexionInternet()) {
            obtenerDatosOnLine();
            pelisArrayList.clear();
            pelisArrayListCopy.clear();
            try {
            for (int i = 0; i < jsonArrayDatosPeli.length(); i++) {
                jsonObjectDatosPelis = jsonArrayDatosPeli.getJSONObject(i).getJSONObject("value");
                ConexionconServer objElimina = new ConexionconServer();
                String resp = objElimina.execute(u.url_mto +
                        jsonObjectDatosPelis.getString("_id") + "?rev=" +
                        jsonObjectDatosPelis.getString("_rev"), "DELETE"
                ).get();  }

                miconexion = new DB(getApplicationContext(), "", null, 1);
                datospelicursor = miconexion.administracion_de_pelis("consultar", null);
                if( datospelicursor.moveToFirst() ){
                    JSONObject datospelis = new JSONObject();

                    do {
                        datospelis.put("titulo",datospelicursor.getString(1));
                        datospelis.put("sinopsis",datospelicursor.getString(2));
                        datospelis.put("duracion",datospelicursor.getString(3));
                        datospelis.put("precio",datospelicursor.getString(4));
                        datospelis.put("urlfoto",datospelicursor.getString(5));
                        datospelis.put("urltriler",datospelicursor.getString(6));
                        enviarDatos guardarpelis = new enviarDatos(getApplicationContext());
                        String resp = guardarpelis.execute(datospelis.toString()).get();

                    } while (datospelicursor.moveToNext());
                } else {
                    mensajes("No hay datos");
                }

            } catch (Exception e) {
           mensajes(e.getMessage());
            }
           //obtenerDatos();
        }
       }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
        try {
            if(di.hayConexionInternet()) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                datospelicursor.moveToPosition(adapterContextMenuInfo.position);
                position = adapterContextMenuInfo.position;
               menu.setHeaderTitle(jsonArrayDatosPeli.getJSONObject(position).getJSONObject("value").getString("titulo"));
            } else {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
                datospelicursor.moveToPosition(adapterContextMenuInfo.position);
               menu.setHeaderTitle(datospelicursor.getString(1));
              }
            idlocal = datospelicursor.getString(0);
              }catch (Exception e){
            mensajes(e.getMessage());
        }
    }
    @Override

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.mxnAgregar:
                    Agregar("nuevo");
                    break;
                case R.id.mxnModificar:
                    Modificar ("modificar");
                    break;
                case R.id.mxnBorrar:
                    Eliminar();
                    break;
                case R.id.mxnInf:
                    ver("datos");
                    break;
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }

    private void ver(String datos) {
        Bundle parametros = new Bundle();
        parametros.putString("accion","ver" );
        parametros.putString("idlocal", idlocal);
        jsonObjectDatosPelis = new JSONObject();
        JSONObject jsonValueObject = new JSONObject();
        if(di.hayConexionInternet())
        {
            try {
                if(jsonArrayDatosPeli.length()>0){
                    parametros.putString("datos", jsonArrayDatosPeli.getJSONObject(position).toString() );
                }
            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }else{
            try {
                jsonArrayDatosPeli = new JSONArray();
                jsonObjectDatosPelis.put("_id", datospelicursor.getString(0));
                jsonObjectDatosPelis.put("_rev", datospelicursor.getString(0));
                jsonObjectDatosPelis.put("titulo", datospelicursor.getString(1));
                jsonObjectDatosPelis.put("sinopsis", datospelicursor.getString(2));
                jsonObjectDatosPelis.put("duracion", datospelicursor.getString(3));
                jsonObjectDatosPelis.put("precio", datospelicursor.getString(4));
                jsonObjectDatosPelis.put("urlfoto", datospelicursor.getString(5));
                jsonObjectDatosPelis.put("urltriler", datospelicursor.getString(6));
                jsonValueObject.put("value", jsonObjectDatosPelis);
                jsonArrayDatosPeli.put(jsonValueObject);
                if(jsonArrayDatosPeli.length()>0){
                    parametros.putString("datos", jsonArrayDatosPeli.getJSONObject(position).toString() );
                }

            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }
        Intent i = new Intent(getApplicationContext(), vistaPeliculas.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void Eliminar(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
            confirmacion.setTitle("¿Esta seguro en eliminar?");
            if (di.hayConexionInternet())
            {
                jsonObjectDatosPelis = jsonArrayDatosPeli.getJSONObject(position).getJSONObject("value");
                confirmacion.setMessage(jsonObjectDatosPelis.getString("titulo"));
            }else {
                confirmacion.setMessage(datospelicursor.getString(1));
            }

            confirmacion.setPositiveButton("Si", (dialog, which) -> {

                try {
                    if(di.hayConexionInternet()){
                        ConexionconServer objElimina = new ConexionconServer();
                        String resp =  objElimina.execute(u.url_mto +
                                jsonObjectDatosPelis.getString("_id")+ "?rev="+
                                jsonObjectDatosPelis.getString("_rev"), "DELETE"
                        ).get();

                        JSONObject jsonRespEliminar = new JSONObject(resp);
                        if(jsonRespEliminar.getBoolean("ok")){
                          jsonArrayDatosPeli.remove(position);
                         mostrarDatos();
                       }
                    }

                    miconexion = new DB(getApplicationContext(), "", null, 1);
                    datospelicursor = miconexion.eliminar("eliminar", datospelicursor.getString(0));
                    obtenerDatos();
                    mensajes("Registro eliminado");
                    dialog.dismiss();
                }catch (Exception e){
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                mensajes("Eliminacion cancelada");
                dialog.dismiss();
            });
            confirmacion.create().show();
        } catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void Buscar() {
        TextView tempVal = findViewById(R.id.txtbuscar);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pelisArrayList.clear();
                if (tempVal.getText().toString().length()<1){
                    pelisArrayList.addAll(pelisArrayListCopy);
                } else{
                    for (peliculas PB : pelisArrayListCopy){
                        String Titulo = PB.getTitulo();
                        String sinopsis = PB.getSinopsis();
                        String duracion = PB.getDuracion();
                        String precio = PB.getPrecio();
                        String buscando = tempVal.getText().toString().trim().toLowerCase();
                        if(sinopsis.toLowerCase().contains(buscando) ||
                                Titulo.toLowerCase().contains(buscando) ||
                                sinopsis.toLowerCase().contains(buscando) ||
                                duracion.toLowerCase().contains(buscando) ||
                                precio.toLowerCase().contains(buscando)){
                            pelisArrayList.add(PB);
                        }
                    }
                }
                adaptadordeimagenes adaptadorImagenes = new adaptadordeimagenes(getApplicationContext(), pelisArrayList);
                Ltspeli.setAdapter(adaptadorImagenes);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }




    private void Modificar(String accion){
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        parametros.putString("idlocal", idlocal);
        jsonObjectDatosPelis = new JSONObject();
        JSONObject jsonValueObject = new JSONObject();
        if(di.hayConexionInternet())
        {
            try {
                if(jsonArrayDatosPeli.length()>0){
                    parametros.putString("datos", jsonArrayDatosPeli.getJSONObject(position).toString() );
                }
            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }else{
            try {
                jsonArrayDatosPeli = new JSONArray();
                jsonObjectDatosPelis.put("_id", datospelicursor.getString(0));
                jsonObjectDatosPelis.put("_rev", datospelicursor.getString(0));
                jsonObjectDatosPelis.put("titulo", datospelicursor.getString(1));
                jsonObjectDatosPelis.put("sinopsis", datospelicursor.getString(2));
                jsonObjectDatosPelis.put("duracion", datospelicursor.getString(3));
                jsonObjectDatosPelis.put("precio", datospelicursor.getString(4));
                jsonObjectDatosPelis.put("urlfoto", datospelicursor.getString(5));
                jsonObjectDatosPelis.put("urltriler", datospelicursor.getString(6));
                jsonValueObject.put("value", jsonObjectDatosPelis);
                jsonArrayDatosPeli.put(jsonValueObject);
                if(jsonArrayDatosPeli.length()>0){
                    parametros.putString("datos", jsonArrayDatosPeli.getJSONObject(position).toString() );
                }

            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }
        Intent i = new Intent(getApplicationContext(), agregarpeliculas.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void Agregar(String accion){
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarpeliculas.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void obtenerDatosOffLine(){
        try {
            miconexion = new DB(getApplicationContext(), "", null, 1);
            datospelicursor = miconexion.administracion_de_pelis("consultar", null);
            if( datospelicursor.moveToFirst() ){
             mostrarDatos();
            } else {
                mensajes("no hay datos");
            }
        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }

    private void obtenerDatosOnLine(){
        try {
            ConexionconServer conexionconServer = new ConexionconServer();
            String resp = conexionconServer.execute(u.urlServer, "GET").get();
            jsonObjectDatosPelis=new JSONObject(resp);
            jsonArrayDatosPeli = jsonObjectDatosPelis.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void obtenerDatos(){
        if(di.hayConexionInternet()) {
            mensajes("Mostrando datos online");
            obtenerDatosOnLine();
            obtenerDatosOffLine();
        } else {
           mensajes("Mostrando datos locales");
           obtenerDatosOffLine();
        }
    }

    private void mostrarDatos(){
        try{
           Ltspeli = findViewById(R.id.listpelis);
            pelisArrayList.clear();
            pelisArrayListCopy.clear();
            JSONObject jsonObject;
            if(di.hayConexionInternet()) {
                if(jsonArrayDatosPeli.length()>0) {
                    for (int i = 0; i < jsonArrayDatosPeli.length(); i++) {
                        jsonObject = jsonArrayDatosPeli.getJSONObject(i).getJSONObject("value");
                        misPelis = new peliculas(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("sinopsis"),
                                jsonObject.getString("duracion"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("urlfoto"),
                                jsonObject.getString("urltriler")

                        );
                        pelisArrayList.add(misPelis);
                    }}
                 } else {
                do{
                    misPelis = new peliculas(
                            datospelicursor.getString(0),//
                            datospelicursor.getString(1),//
                            datospelicursor.getString(1),//
                            datospelicursor.getString(2),//
                            datospelicursor.getString(3),//
                            datospelicursor.getString(4),//
                            datospelicursor.getString(5), //
                            datospelicursor.getString(6) //
                    );
                    pelisArrayList.add(misPelis);
                }while(datospelicursor.moveToNext());
              }
              adaptadordeimagenes adaptadorImagenes = new adaptadordeimagenes(getApplicationContext(), pelisArrayList);
            Ltspeli.setAdapter(adaptadorImagenes);
            registerForContextMenu(Ltspeli);
            pelisArrayListCopy.addAll(pelisArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }

    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}
