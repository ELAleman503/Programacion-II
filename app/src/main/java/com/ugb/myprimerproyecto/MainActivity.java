package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnadd;
    DB miconexion;
    ListView ltsproductos;
    Cursor datosproductoscursor = null;
    ArrayList<productos> productosArrayList=new ArrayList<productos>();
    ArrayList<productos> productosArrayListCopy=new ArrayList<productos>();
    productos misProductos;
    JSONArray jsonArrayDatosProductos;
    JSONObject jsonObjectDatosProductos;
    utilidades u;
    detectarInternet di;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        di = new detectarInternet(getApplicationContext());
        btnadd = findViewById(R.id.btnagregarproducto);
        btnadd.setOnClickListener(v->{
            agregaproducto("nuevo");
        });
        obtenerDatos();
        buscarProductos();
       }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_productos, menu);
        try {


            if(di.hayConexionInternet()) {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
                position = adapterContextMenuInfo.position;

                menu.setHeaderTitle(jsonArrayDatosProductos.getJSONObject(position).getJSONObject("value").getString("descripcion"));

            } else {
                AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
                datosproductoscursor.moveToPosition(adapterContextMenuInfo.position);

                menu.setHeaderTitle(datosproductoscursor.getString(2));
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
                    agregaproducto("nuevo");
                    break;
                case R.id.mxnModificar:
                    ModificarProductos ("modificar");
                    break;
                case R.id.mxnEliminar:
                    eliminarProducto();
                    break;
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
    }

    private void eliminarProducto(){
        try {
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(MainActivity.this);
            confirmacion.setTitle("Esta seguro de eliminar?");

if (di.hayConexionInternet()){
    jsonObjectDatosProductos = jsonArrayDatosProductos.getJSONObject(position).getJSONObject("value");
    confirmacion.setMessage(jsonObjectDatosProductos.getString("descripcion"));

}else {
    confirmacion.setMessage(datosproductoscursor.getString(2));
}
            confirmacion.setPositiveButton("Si", (dialog, which) -> {

                try {
                    if(di.hayConexionInternet()){
                        ConexionconServer objElimina = new ConexionconServer();
                        String resp =  objElimina.execute(u.url_mto +
                                jsonObjectDatosProductos.getString("_id")+ "?rev="+
                                jsonObjectDatosProductos.getString("_rev"), "DELETE"
                        ).get();
                        JSONObject jsonRespEliminar = new JSONObject(resp);
                        if(jsonRespEliminar.getBoolean("ok")){
                            jsonArrayDatosProductos.remove(position);
                            mostrarDatos();
                        }
                    }
                    else{
                        miconexion = new DB(getApplicationContext(), "", null, 1);
                        datosproductoscursor = miconexion.administracion_de_productos("eliminar", new String[]{datosproductoscursor.getString(0)});

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



    private void buscarProductos() {
        TextView tempVal = findViewById(R.id.txtbuscarproducto);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productosArrayList.clear();
                if (tempVal.getText().toString().length()<1){
                    productosArrayList.addAll(productosArrayListCopy);
                } else{
                    for (productos PB : productosArrayListCopy){
                        String nombre = PB.getDescripcion();
                        String codigo = PB.getCodigo();
                        String marca = PB.getMarca();
                        String presentacion = PB.getPresentacion();
                        String precio = PB.getPrecio();
                        String buscando = tempVal.getText().toString().trim().toLowerCase();
                        if(nombre.toLowerCase().contains(buscando) ||
                                codigo.toLowerCase().contains(buscando) ||
                                marca.toLowerCase().contains(buscando) ||
                                presentacion.toLowerCase().contains(buscando) ||
                                precio.toLowerCase().contains(buscando)){
                            productosArrayList.add(PB);
                        }
                    }
                }
                adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
                ltsproductos.setAdapter(adaptadorImagenes);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void ModificarProductos(String accion){
       if(di.hayConexionInternet()){
           try {
               Bundle parametros = new Bundle();
               parametros.putString("accion", accion);

               if(jsonArrayDatosProductos.length()>0){
                   parametros.putString("datos", jsonArrayDatosProductos.getJSONObject(position).toString() );

               }

               Intent i = new Intent(getApplicationContext(), agregarproductos.class);
               i.putExtras(parametros);
               startActivity(i);

           }catch (Exception e){
               mensajes(e.getMessage());
           }
       } else{

try {
    Bundle parametros = new Bundle();
    parametros.putString("accion", accion);
    jsonObjectDatosProductos = new JSONObject();
    JSONObject jsonValueObject = new JSONObject();
    jsonArrayDatosProductos = new JSONArray();

    jsonObjectDatosProductos.put("_id", datosproductoscursor.getString(0));
    jsonObjectDatosProductos.put("_rev", datosproductoscursor.getString(0));
    jsonObjectDatosProductos.put("codigo", datosproductoscursor.getString(1));
    jsonObjectDatosProductos.put("descripcion", datosproductoscursor.getString(2));
    jsonObjectDatosProductos.put("marca", datosproductoscursor.getString(3));
    jsonObjectDatosProductos.put("presentacion", datosproductoscursor.getString(4));
    jsonObjectDatosProductos.put("precio", datosproductoscursor.getString(5));
    jsonObjectDatosProductos.put("urlfoto", datosproductoscursor.getString(6));
    jsonValueObject.put("value", jsonObjectDatosProductos);

    jsonArrayDatosProductos.put(jsonValueObject);

    if(jsonArrayDatosProductos.length()>0){
        parametros.putString("datos", jsonArrayDatosProductos.getJSONObject(position).toString() );

    }

    Intent i = new Intent(getApplicationContext(), agregarproductos.class);
    i.putExtras(parametros);
    startActivity(i);


}catch (Exception e){
mensajes(e.getMessage());
}



       }
    }

    private void agregaproducto(String accion){
        Bundle parametros = new Bundle();
        parametros.putString("accion", accion);


        Intent i = new Intent(getApplicationContext(), agregarproductos.class);
        i.putExtras(parametros);
        startActivity(i);


    }
    private void obtenerDatosProductosOffLine(){
        try {

            miconexion = new DB(getApplicationContext(), "", null, 1);
            datosproductoscursor = miconexion.administracion_de_productos("consultar", null);

            if( datosproductoscursor.moveToFirst() ){

             mostrarDatos();



            } else {
                //sino enviar a guardar datos
                mensajes("No hay datos");
            }

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }



    private void obtenerDatosProductosOnLine(){
        try {
            ConexionconServer conexionconServer = new ConexionconServer();
            String resp = conexionconServer.execute(u.urlServer, "GET").get();

            jsonObjectDatosProductos=new JSONObject(resp);
            jsonArrayDatosProductos = jsonObjectDatosProductos.getJSONArray("rows");
            mostrarDatos();
        }catch (Exception ex){
            mensajes(ex.getMessage());
        }
    }

    private void obtenerDatos(){
        if(di.hayConexionInternet()) {
         mensajes("Mostrando datos desde la nube");
        obtenerDatosProductosOnLine();
    } else {
           mensajes("Mostrando datos locales");

            obtenerDatosProductosOffLine();
        }
    }


    private void mostrarDatos(){
        try{
           ltsproductos = findViewById(R.id.listproductos);
            productosArrayList.clear();
            productosArrayListCopy.clear();
            JSONObject jsonObject;

            if(di.hayConexionInternet()) {
                if(jsonArrayDatosProductos.length()>0) {
                    for (int i = 0; i < jsonArrayDatosProductos.length(); i++) {
                        jsonObject = jsonArrayDatosProductos.getJSONObject(i).getJSONObject("value");
                        misProductos = new productos(
                                jsonObject.getString("_id"),
                                jsonObject.getString("_rev"),
                                jsonObject.getString("codigo"),
                                jsonObject.getString("descripcion"),
                                jsonObject.getString("marca"),
                                jsonObject.getString("presentacion"),
                                jsonObject.getString("precio"),
                                jsonObject.getString("urlfoto")
                        );
                        productosArrayList.add(misProductos);

                    }}
                 } else {


                do{
                    misProductos = new productos(
                            datosproductoscursor.getString(0),//idproducto
                            datosproductoscursor.getString(1),//codigo
                            datosproductoscursor.getString(1),//codigo
                            datosproductoscursor.getString(2),//descripcion
                            datosproductoscursor.getString(3),//marca
                            datosproductoscursor.getString(4),//presentacion
                            datosproductoscursor.getString(5), //precio
                            datosproductoscursor.getString(6) //urldefoto


                    );
                    productosArrayList.add(misProductos);
                }while(datosproductoscursor.moveToNext());
              }
              adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
            ltsproductos.setAdapter(adaptadorImagenes);
            registerForContextMenu(ltsproductos);
            productosArrayListCopy.addAll(productosArrayList);

        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }


    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }



    private class ConexionconServer extends AsyncTask<String, String, String>{
        HttpURLConnection urlConnection;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... parametros) {
            StringBuilder result = new StringBuilder();
            try{
                String uri = parametros[0];
                String metodo = parametros[1];
                URL url = new URL(uri);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod(metodo);

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String linea;
                while( (linea=bufferedReader.readLine())!=null ){
                    result.append(linea);
                }
            }catch (Exception e){
                Log.i("GET", e.getMessage());
            }
            return result.toString();
        }
    }
}

class productos{
    String idproducto;
    String rev;
    String codigo;
    String descripcion;
    String marca;
    String presentacion;
    String precio;
    String urlfoto;

    public productos(String idproducto, String codigo,  String rev, String descripcion, String marca, String presentacion, String precio, String urlfoto) {
        this.idproducto = idproducto;
        this.rev = rev;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.urlfoto = urlfoto;
    }
    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }
}