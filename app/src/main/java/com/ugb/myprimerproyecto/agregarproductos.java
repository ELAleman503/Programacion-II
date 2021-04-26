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

        miconexion = new DB(getApplicationContext(),"",null,1);

        btnregresar = findViewById(R.id.btnregresar);
        imgfotodeproducto = findViewById(R.id.imgfotoproducto);
        btnagregarproducto = findViewById(R.id.btnguardarproducto);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        imgfotodeproducto.setOnClickListener(v -> {
            tomarfoto();
        });

        btnagregarproducto.setOnClickListener(v -> {
           agregarproducto();
        });

       mostrardatosproducto();

    }

    private void regresarmainactivity() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    private void tomarfoto() {
        tomarfotointent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (tomarfotointent.resolveActivity(getPackageManager()) != null ){

            File mifoto = null;

            try {
                mifoto = crearfoto();
            }catch (Exception e){
                mensajes(e.getMessage());
            }

            if (mifoto != null){
                try{
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
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urldefoto);
                imgfotodeproducto.setImageBitmap(imagenBitmap);
            }
        }catch (Exception e){
            mensajes(e.getMessage());
        }
    }

    private File crearfoto() throws IOException {
        String tiempo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombredeimagen = "img_"+ tiempo +"_";

        File rutadealmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if(!rutadealmacenamiento.exists()){
            rutadealmacenamiento.mkdirs();
        }
        File image = File.createTempFile(nombredeimagen,".jpg",rutadealmacenamiento);
        urldefoto = image.getAbsolutePath();
        return image;
    }

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
            mensajes("Registro guardado");

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
    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}