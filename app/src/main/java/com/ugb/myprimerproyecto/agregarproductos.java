package com.ugb.myprimerproyecto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class agregarproductos extends AppCompatActivity {

    FloatingActionButton btnregresar;
    ImageView imgfotodeproducto;
    Intent tomarfotointent;
    String urldefoto;
    String idproducto, accion = "nuevo", rev;
    Button btnagregarproducto;
    DB miconexion;
    TextView temp;
    utilidades miUrl;
    detectarInternet di;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarproductos);

        //conexion
        miconexion = new DB(getApplicationContext(),"",null,1);

        //relacion entre java y xml
        btnregresar = findViewById(R.id.btnregresar);
        imgfotodeproducto = findViewById(R.id.imgfotoproducto);
        btnagregarproducto = findViewById(R.id.btnguardarproducto);

        //btn atras
        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        //btn tomar foto
        imgfotodeproducto.setOnClickListener(v -> {
            tomarfoto();
        });


        //btn agregar producto
        btnagregarproducto.setOnClickListener(v -> {
           agregarproducto();
        });

       mostrardatosproducto();

    }

    //metodo para regresar a pantalla anterior
    private void regresarmainactivity() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    //metodo de tomar foto
    private void tomarfoto() {
        //lanzar activity de camara
        tomarfotointent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //si la camara fue lanzada exitosamente
        if (tomarfotointent.resolveActivity(getPackageManager()) != null ){

            File mifoto = null;

            try {
                mifoto = crearfoto();
            }catch (Exception e){
                mensajes(e.getMessage());
            }

            //si la foto fue tomada
            if (mifoto != null){
                try{
                    //buscar foto para mostrarla
                    Uri urifotoproducto = FileProvider.getUriForFile(agregarproductos.this, "com.ugb.myprimerproyecto.fileprovider",mifoto);
                    tomarfotointent.putExtra(MediaStore.EXTRA_OUTPUT, urifotoproducto);
                    startActivityForResult(tomarfotointent,1);
                }catch (Exception e){
                    mensajes(e.getMessage());
                }
            } else {
                mensajes("No fue posible tomar la foto");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //si la foto a sido correcta mapear la foto para que sea visible
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urldefoto);
                imgfotodeproducto.setImageBitmap(imagenBitmap);
            }
        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }


    //metodo para rutas y creacion de nombres
    private File crearfoto() throws IOException {
        //nombre de la foto
        String tiempo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombredeimagen = "img_"+ tiempo +"_";

        //Ruta de almacenamiento
        File rutadealmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        //si la ruta de almacenamiento no existe
        if(!rutadealmacenamiento.exists()){
            //crearla
            rutadealmacenamiento.mkdirs();
        }

        //Crear temporal de foto tomada
        File image = File.createTempFile(nombredeimagen,".jpg",rutadealmacenamiento);
        // y guardar ruta
        urldefoto = image.getAbsolutePath();
        return image;
    }

    //Metodo para agregar producto
    private void agregarproducto() {
        try {
            temp = findViewById(R.id.txtcodigo);
            String codigo = temp.getText().toString();

            temp = findViewById(R.id.txtdescripcion);
            String descripcion = temp.getText().toString();

            temp = findViewById(R.id.txtmarca);
            String marca = temp.getText().toString();

            temp = findViewById(R.id.txtpresentacion);
            String presentacion = temp.getText().toString();

            temp = findViewById(R.id.txtprecio);
            String presio = temp.getText().toString();


            JSONObject datosproductos = new JSONObject();
            if(accion.equals("modificar") && idproducto.length()>0 && rev.length()>0 ){
                datosproductos.put("_id",idproducto);
                datosproductos.put("_rev",rev);
            }

            datosproductos.put("codigo",codigo);
            datosproductos.put("descripcion",descripcion);
            datosproductos.put("marca",marca);
            datosproductos.put("presentacion",presentacion);
            datosproductos.put("precio",presio);
            datosproductos.put("urlfoto",urldefoto);

            String[] datos = {idproducto, codigo, descripcion, marca, presentacion, presio, urldefoto };

            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) {
                enviarDatos guardarproducto = new enviarDatos(getApplicationContext());
                String resp = guardarproducto.execute(datosproductos.toString()).get();
            }
            miconexion.administracion_de_productos(accion, datos);
            mensajes("Registro guardado con exito.");

            regresarmainactivity();

        }catch (Exception w){

        }

    }

    private void mostrardatosproducto() {

           try {
               Bundle recibirparametros = getIntent().getExtras();
               accion = recibirparametros.getString("accion");

               if(accion.equals("modificar")){
                   JSONObject datos = new JSONObject(recibirparametros.getString("datos")).getJSONObject("value");

                   idproducto = datos.getString("_id");
                   rev = datos.getString("_rev");

                   temp = findViewById(R.id.txtcodigo);
                   temp.setText(datos.getString("codigo"));

                   temp = findViewById(R.id.txtdescripcion);
                   temp.setText(datos.getString("descripcion"));

                   temp = findViewById(R.id.txtmarca);
                   temp.setText(datos.getString("marca"));

                   temp = findViewById(R.id.txtpresentacion);
                   temp.setText(datos.getString("presentacion"));

                   temp = findViewById(R.id.txtprecio);
                   temp.setText(datos.getString("precio"));

                   urldefoto =  datos.getString("urlfoto");
                   Bitmap bitmap = BitmapFactory.decodeFile(urldefoto);
                   imgfotodeproducto.setImageBitmap(bitmap);
               }
           }catch (Exception ex){
               mensajes(ex.getMessage());
           }


    }


    //metodo para mostrar mensajes
    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}