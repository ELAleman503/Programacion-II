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

public class mostrarvolunt extends AppCompatActivity {

    TextView nombrel, duil;
    FloatingActionButton btnadd;
    ListView ltsvolunt;
    Cursor datosvcursor = null;
    ArrayList<volunt> voluntArrayList =new ArrayList<volunt>();
    ArrayList<volunt> voluntArrayListCopy =new ArrayList<volunt>();
    volunt mispo;
    JSONArray jsonArrayDatosvolunt;
    JSONObject jsonObjectDatosvolunt;
    utilidades u;
    String lognombre,logdui,logtelefono,logmail,logpadss;
    detectarInternet di;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarvolunt);

        nombrel = findViewById(R.id.nombre);
        duil = findViewById(R.id.duii);

        Bundle recibirparametros = getIntent().getExtras();
        lognombre = recibirparametros.getString("nombre");
        logdui = recibirparametros.getString("duii");
        logtelefono = recibirparametros.getString("telefono");
        logmail = recibirparametros.getString("mail");
        logpadss = recibirparametros.getString("padss");

        nombrel.setText(lognombre);

        di = new detectarInternet(getApplicationContext());
        btnadd = findViewById(R.id.btnagregar);
        btnadd.setOnClickListener(v->{
            Agregar("nuevo");
        });

        obtenerDatos();
         Buscar();
    }

    private void Buscar() {
        TextView tempVal = findViewById(R.id.txtbuscar);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                voluntArrayList.clear();
                if (tempVal.getText().toString().length()<1){
                    voluntArrayList.addAll(voluntArrayListCopy);
                } else{
                    for (volunt PB : voluntArrayListCopy){
                        String nombrevolunt = PB.getNombrevolunt();
                        String nombrevolun = PB.getPdonar();
                        String Otros = PB.getOtros();
                        String Dui = PB.getDuipo();
                        String buscando = tempVal.getText().toString().trim().toLowerCase();
                        if(nombrevolunt.toLowerCase().contains(buscando) ||
                                nombrevolun.toLowerCase().contains(buscando) ||
                                Otros.toLowerCase().contains(buscando) ||
                                Dui.toLowerCase().contains(buscando) ){
                            voluntArrayList.add(PB);
                        }
                    }
                }
                adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), voluntArrayList);
                ltsvolunt.setAdapter(adaptadorImagenes);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });}

    private void Agregar(String accion) {
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        parametros.putString("nombre", lognombre);
        parametros.putString("duii", logdui);
        parametros.putString("telefono", logtelefono);
        parametros.putString("mail", logmail);
        parametros.putString("padss", logpadss);
        Intent i = new Intent(getApplicationContext(), agregarvolunt.class);
        i.putExtras(parametros);
        startActivity(i);

    }

    private void obtenerDatos() {
        if(di.hayConexionInternet()) {
            mensajes("Mostrando datos");
            obtenerDatosOnLine();
           } else {
            mensajes("Acceso no permido a la base de datos");
            }
    }

    private void obtenerDatosOnLine() {
        try {
            ConexionconServer conexionconServer = new ConexionconServer();
            String resp = conexionconServer.execute(u.urlmostrarvoluntarios, "GET").get();
            jsonObjectDatosvolunt=new JSONObject(resp);
            jsonArrayDatosvolunt = jsonObjectDatosvolunt.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void mostrarDatos() {
        try{
            ltsvolunt = findViewById(R.id.list);
            voluntArrayList.clear();
            voluntArrayListCopy.clear();
            JSONObject jsonObject;
            if(di.hayConexionInternet()) {
                if(jsonArrayDatosvolunt.length()>0) {
                    for (int i = 0; i < jsonArrayDatosvolunt.length(); i++) {
                        jsonObject = jsonArrayDatosvolunt.getJSONObject(i).getJSONObject("value");
                        mispo = new volunt(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("dui"),
                                jsonObject.getString("donacion"),
                                jsonObject.getString("dirreccion"),
                                jsonObject.getString("urlfoto"),
                                jsonObject.getString("urltriler")
                        );
                        voluntArrayList.add(mispo);
                    }}
            }

            adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), voluntArrayList);
            ltsvolunt.setAdapter(adaptadorImagenes);
            registerForContextMenu(ltsvolunt);
            voluntArrayListCopy.addAll(voluntArrayList);

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
                menu.setHeaderTitle(jsonArrayDatosvolunt.getJSONObject(position).getJSONObject("value").getString("nombre"));
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
                   Modificar ("modificar");
                    break;
                case R.id.mxnEliminar:
                    Eliminar();
                    break;


            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }

    private void Eliminar(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(mostrarvolunt.this);
            confirmacion.setTitle("¿Quieres Eliminar?");

                jsonObjectDatosvolunt = jsonArrayDatosvolunt.getJSONObject(position).getJSONObject("value");
                confirmacion.setMessage(jsonObjectDatosvolunt.getString("nombre"));

            confirmacion.setPositiveButton("Si", (dialog, which) -> {

                try {
                    if(di.hayConexionInternet()){
                        ConexionconServer objElimina = new ConexionconServer();
                        String resp =  objElimina.execute(u.urlagregarVoluntarios +
                                jsonObjectDatosvolunt.getString("_id")+ "?rev="+
                                jsonObjectDatosvolunt.getString("_rev"), "DELETE"
                        ).get();

                        JSONObject jsonRespEliminar = new JSONObject(resp);
                        if(jsonRespEliminar.getBoolean("ok")){
                            jsonArrayDatosvolunt.remove(position);
                            mostrarDatos();
                        }
                    }

                    obtenerDatos();
                    mensajes("Registro eliminado");
                    dialog.dismiss();
                }catch (Exception e){
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                mensajes("Se detuvo");
                dialog.dismiss();
            });
            confirmacion.create().show();
        } catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }


    private void Modificar(String accion){
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);
        parametros.putString("nombre", lognombre);
        parametros.putString("duii", logdui);
        parametros.putString("telefono", logtelefono);
        parametros.putString("mail", logmail);
        parametros.putString("padss", logpadss);
        jsonObjectDatosvolunt = new JSONObject();
        JSONObject jsonValueObject = new JSONObject();
        if(di.hayConexionInternet())
        {
            try {
                if(jsonArrayDatosvolunt.length()>0){
                    parametros.putString("datos", jsonArrayDatosvolunt.getJSONObject(position).toString() );
                }
            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }
        Intent i = new Intent(getApplicationContext(), agregarvolunt.class);
        i.putExtras(parametros);
        startActivity(i);
    }




}