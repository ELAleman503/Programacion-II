package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class mostrarvoluntarios extends AppCompatActivity {

    TextView nombrel, duil;
    FloatingActionButton btnadd;
    DB miconexion;
    ListView ltspostulados;
    Cursor datosvoluntarioscursor = null;
    ArrayList<voluntarios> voluntariosArrayList=new ArrayList<voluntarios>();
    ArrayList<voluntarios> voluntariosArrayListCopy=new ArrayList<voluntarios>();
    voluntarios misvoluntarios;
    JSONArray jsonArrayDatosvoluntarios;
    JSONObject jsonObjectDatosvoluntarios;
    utilidades u;
    String lognombre,logdui,logtelefono,logmail,logpadss;
    detectarInternet di;
    int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarvoluntarios);

        Bundle recibirparametros = getIntent().getExtras();
        lognombre = recibirparametros.getString("nombre");
        logdui = recibirparametros.getString("duii");
        logtelefono = recibirparametros.getString("telefono");
        logmail = recibirparametros.getString("mail");
        logpadss = recibirparametros.getString("padss");

        nombrel.setText(lognombre);
        duil.setText(logdui);

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
        parametros.putString("nombre", lognombre);
        parametros.putString("duii", logdui);
        parametros.putString("telefono", logtelefono);
        parametros.putString("mail", logmail);
        parametros.putString("padss", logpadss);
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
            String resp = conexionconServer.execute(u.urlmostrarvoluntarios, "GET").get();
            jsonObjectDatosvoluntarios=new JSONObject(resp);
            jsonArrayDatosvoluntarios = jsonObjectDatosvoluntarios.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
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
                                jsonObject.getString("propuesta"),
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
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(mostrarvoluntarios.this);
            confirmacion.setTitle("Esta seguro de eliminar?");

            jsonObjectDatosvoluntarios = jsonArrayDatosvoluntarios.getJSONObject(position).getJSONObject("value");
            confirmacion.setMessage(jsonObjectDatosvoluntarios.getString("nombre"));

            confirmacion.setPositiveButton("Si", (dialog, which) -> {

                try {
                    if(di.hayConexionInternet()){
                        ConexionconServer objElimina = new ConexionconServer();
                        String resp =  objElimina.execute(u.urlagregarVoluntarios +
                                jsonObjectDatosvoluntarios.getString("_id")+ "?rev="+
                                jsonObjectDatosvoluntarios.getString("_rev"), "DELETE"
                        ).get();

                        JSONObject jsonRespEliminar = new JSONObject(resp);
                        if(jsonRespEliminar.getBoolean("ok")){
                            jsonArrayDatosvoluntarios.remove(position);
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
                mensajes("Eliminacion detendia");
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
        jsonObjectDatosvoluntarios = new JSONObject();
        JSONObject jsonValueObject = new JSONObject();
        if(di.hayConexionInternet())
        {
            try {
                if(jsonArrayDatosvoluntarios.length()>0){
                    parametros.putString("datos", jsonArrayDatosvoluntarios.getJSONObject(position).toString() );
                }
            }catch (Exception e){
                mensajes(e.getMessage());
            }
        }
        Intent i = new Intent(getApplicationContext(), agregarVoluntarios.class);
        i.putExtras(parametros);
        startActivity(i);
    }


}
