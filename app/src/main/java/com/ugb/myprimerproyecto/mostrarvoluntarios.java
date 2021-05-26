package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class mostrarvoluntarios extends AppCompatActivity {
    public static  final String nombre="nombre";
    public static  final String duii="duii";
    public static  final String telefono="telefono";
    public static  final String mail="mail";
    public static  final String padss="padss";

    TextView nombrel, duil, telefotol,maill,padssl;


    FloatingActionButton btnadd;
    DB miconexion;
    ListView ltspostulados;
    //Cursor datosvoluntarioscursor = null;
    ArrayList<voluntarios> voluntariosArrayList=new ArrayList<voluntarios>();
    ArrayList<voluntarios> voluntariosArrayListCopy=new ArrayList<voluntarios>();
    voluntarios misvoluntarios;
    JSONArray jsonArrayDatosvoluntarios;
    JSONObject jsonObjectDatosvoluntarios;
    utilidades utils;
    String idlocal;
    detectarInternet di;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarvoluntarios);

        nombrel = findViewById(R.id.nombre);
        duil = findViewById(R.id.duii);

        nombrel.setText(getIntent().getStringExtra("nombre"));
        duil.setText(getIntent().getStringExtra("mail"));

        di = new detectarInternet(getApplicationContext());
        btnadd = findViewById(R.id.btnagregar);
        btnadd.setOnClickListener(v->{
            Agregar("nuevo");
        });

        obtenerDatos();

    }

    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        Intent i = new Intent(getApplicationContext(), agregarVoluntarios.class);
        i.putExtras(parametros);
        startActivity(i);
    }

    private void obtenerDatos() {
        if(di.hayConexionInternet()) {
            mensajes("Mostrando datos de voluntarios");
            obtenerDatosOnLine();
           } else {
            mensajes("No se pudo conectar data base");
            }
    }

    private void obtenerDatosOnLine() {
        try {
            ConexionconServer conexionconServer = new ConexionconServer();
            String resp = conexionconServer.execute(utils.urlmostrarvoluntarios, "GET").get();
            jsonObjectDatosvoluntarios=new JSONObject(resp);
            jsonArrayDatosvoluntarios = jsonObjectDatosvoluntarios.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){

        }
    }

    private void mostrarDatos() {
        try{
            ltspostulados = findViewById(R.id.list);
            voluntariosArrayList.clear();
            voluntariosArrayListCopy.clear();
            JSONObject jsonObject;
            if(di.hayConexionInternet()) {
                if(jsonArrayDatosvoluntarios.length()>0) {
                    for (int i = 0; i < jsonArrayDatosvoluntarios.length(); i++) {
                        jsonObject = jsonArrayDatosvoluntarios.getJSONObject(i).getJSONObject("value");
                        misvoluntarios = new voluntarios(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("dui"),
                                jsonObject.getString("donacion"),
                                jsonObject.getString("otro"),
                                jsonObject.getString("urlfoto"),
                                jsonObject.getString("urltriler")
                        );
                        voluntariosArrayList.add(misvoluntarios);
                    }}
            }

            adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), voluntariosArrayList);
            ltspostulados.setAdapter(adaptadorImagenes);
            registerForContextMenu(ltspostulados);
            voluntariosArrayListCopy.addAll(voluntariosArrayList);

        }catch (Exception e){

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
                menu.setHeaderTitle(jsonArrayDatosvoluntarios.getJSONObject(position).getJSONObject("value").getString("nombre"));
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
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }
}