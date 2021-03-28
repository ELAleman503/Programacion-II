package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
// boton flotante
    FloatingActionButton btnadd;
    DB miconexion;
    ListView listaproductos;
    Cursor datosproductoscursor = null;
    ArrayList<productos> productosArrayList = new ArrayList<productos>();
    ArrayList<productos> productosArrayListcopy = new ArrayList<productos>();
    productos misproductos;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //relacionamos con xml
        btnadd = findViewById(R.id.btnagregarproducto);

        //cargar datos al abrir aplicacion
            comprobardatos();

        //evento de tocar el boton agregar producto
       btnadd.setOnClickListener(v-> {
          //metodo para lanzar activity agregar producto
           agregaproductos("nuevo", new String[]{});
       });
        buscarProductos();

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_productos,menu);

        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
        datosproductoscursor.moveToPosition(adapterContextMenuInfo.position);
        menu.setHeaderTitle(datosproductoscursor.getString(1));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try {

            switch (item.getItemId()) {
                case R.id.mxnAgregar:
                    agregaproductos("nuevo", new String[]{});
                    break;
                case R.id.mxnModificar:
                    String[] datos = {
                            datosproductoscursor.getString(0),
                            datosproductoscursor.getString(1),
                            datosproductoscursor.getString(2),
                            datosproductoscursor.getString(3),
                            datosproductoscursor.getString(4),
                            datosproductoscursor.getString(5),
                            datosproductoscursor.getString(6)
                    };
                    agregaproductos("modificar", datos);
                    break;
                case R.id.mxnEliminar:


                    break;
            }
        } catch (Exception ex) {
            mensajes(ex.getMessage());
        }
        return super.onContextItemSelected(item);
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
                    productosArrayList.addAll(productosArrayListcopy);
                } else{
                    for (productos PB : productosArrayListcopy){
                        String nombre = PB.getDescripcion();
                        if(nombre.toLowerCase().contains(tempVal.getText().toString().trim().toLowerCase())){
                            productosArrayList.add(PB);
                        }
                    }
                }
                adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
                listaproductos.setAdapter(adaptadorImagenes);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    //metodo de lanzar activity
    private void agregaproductos(String accion, String[] datos) {
        try {
            Bundle parametroProductos = new Bundle();
            parametroProductos.putString("accion", accion);


            parametroProductos.putStringArray("datos",datos );
            //lanzar activity de agregar producto

            Intent i = new Intent(getApplicationContext(), agregarproductos.class);
            i.putExtras(parametroProductos);
            startActivity(i);
        }catch (Exception ex){
            mensajes(ex.getMessage());
    }}


    //metodo para comprobar si hay datos
    private void comprobardatos() {
        miconexion = new DB(getApplicationContext(),"",null,1);
        datosproductoscursor = miconexion.administracion_de_productos("consultar",null);

        if( datosproductoscursor.moveToFirst() ){
            //si se encuemtran datos cargarlos
            mostrarmisproductos();
        } else {
            //sino enviar a guardar datos
            mensajes("No hay datos");
            agregaproductos("nuevo", new String[]{});
        }
    }

    //metodos para mostrar los datos encontrados con imagen a la par
    private void mostrarmisproductos() {
        listaproductos = findViewById(R.id.listproductos);
        productosArrayList.clear();
        productosArrayListcopy.clear();

        do{
            misproductos = new productos(
                    datosproductoscursor.getString(0),//idproducto
                    datosproductoscursor.getString(1),//codigo
                    datosproductoscursor.getString(2),//descripcion
                    datosproductoscursor.getString(3),//marca
                    datosproductoscursor.getString(4),//presentacion
                    datosproductoscursor.getString(5), //precio
                    datosproductoscursor.getString(6) //urldefoto
            );
            productosArrayList.add(misproductos);
        }while(datosproductoscursor.moveToNext());
        adaptadorImagenes adaptadorImagenes = new adaptadorImagenes(getApplicationContext(), productosArrayList);
        listaproductos.setAdapter(adaptadorImagenes);

        registerForContextMenu(listaproductos);
        productosArrayListcopy.addAll(productosArrayList);
    }



    class productos{
        String idproducto;
        String codigo;
        String descripcion;
        String marca;
        String presentacion;
        String precio;
        String urlfoto;

        public productos(String idproducto, String codigo, String descripcion, String marca, String presentacion, String precio, String urlfoto) {
            this.idproducto = idproducto;
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

    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}