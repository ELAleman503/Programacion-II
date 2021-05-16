package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

public class mostrarpostulados extends AppCompatActivity {
    public static  final String nombre="nombre";
    public static  final String duii="duii";
    public static  final String telefono="telefono";
    public static  final String mail="mail";
    public static  final String padss="padss";

    TextView nombrel, duil, telefotol,maill,padssl;


    FloatingActionButton btnadd;
    DB miconexion;
    ListView ltspostulados;
    Cursor datospostuladoscursor = null;
    ArrayList<postulados> postuladosArrayList=new ArrayList<postulados>();
    ArrayList<postulados> postuladosArrayListCopy=new ArrayList<postulados>();
    postulados mispostulados;
    JSONArray jsonArrayDatospostulados;
    JSONObject jsonObjectDatospostulados;
    utilidades u;
    String idlocal;
    detectarInternet di;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarpostulados);

        nombrel = findViewById(R.id.nombre);
        duil = findViewById(R.id.duii);
     //   telefotol = findViewById(R.id.telefono);
       // maill = findViewById(R.id.mail);
      //  padssl = findViewById(R.id.padss);

        nombrel.setText(getIntent().getStringExtra("nombre"));
        duil.setText(getIntent().getStringExtra("duii"));
       // telefotol.setText(getIntent().getStringExtra("telefono"));
    //    maill.setText(getIntent().getStringExtra("mail"));
     //   padssl.setText(getIntent().getStringExtra("padss"));


        di = new detectarInternet(getApplicationContext());
        btnadd = findViewById(R.id.btnagregar);
        btnadd.setOnClickListener(v->{
            Agregar("nuevo");
        });

        obtenerDatos();
       // Buscar();
    }

    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarpostulados.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void obtenerDatos() {
        if(di.hayConexionInternet()) {
            mensajes("Mostrando datos de votacion");
            obtenerDatosOnLine();
           } else {
            mensajes("No se pudo conectar con la base");
            }
    }

    private void obtenerDatosOnLine() {
        try {
            ConexionconServer conexionconServer = new ConexionconServer();
            String resp = conexionconServer.execute(u.urlmostrarpostulados, "GET").get();
            jsonObjectDatospostulados=new JSONObject(resp);
            jsonArrayDatospostulados = jsonObjectDatospostulados.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void mostrarDatos() {
        try{
            ltspostulados = findViewById(R.id.list);
            postuladosArrayList.clear();
            postuladosArrayListCopy.clear();
            JSONObject jsonObject;
            if(di.hayConexionInternet()) {
                if(jsonArrayDatospostulados.length()>0) {
                    for (int i = 0; i < jsonArrayDatospostulados.length(); i++) {
                        jsonObject = jsonArrayDatospostulados.getJSONObject(i).getJSONObject("value");
                        mispostulados = new postulados(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("dui"),
                                jsonObject.getString("propuesta"),
                                jsonObject.getString("otro"),
                                jsonObject.getString("urlfoto"),
                                jsonObject.getString("urltriler")
                        );
                        postuladosArrayList.add(mispostulados);
                    }}
            }

            adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), postuladosArrayList);
            ltspostulados.setAdapter(adaptadorImagenes);
            registerForContextMenu(ltspostulados);
            postuladosArrayListCopy.addAll(postuladosArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }
    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edit, menu);
        try {
            if(di.hayConexionInternet()) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                position = adapterContextMenuInfo.position;
                menu.setHeaderTitle(jsonArrayDatospostulados.getJSONObject(position).getJSONObject("value").getString("nombre"));
            }
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
                  //  Modificar ("modificar");
                    break;
                case R.id.mxnEliminar:
                  //  Eliminar();
                    break;
                case R.id.mxnVotar:
                 //   ver("datos");
                    break;
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }
}