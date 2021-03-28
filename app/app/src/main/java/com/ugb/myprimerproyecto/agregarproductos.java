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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class agregarproductos extends AppCompatActivity {

    FloatingActionButton btnregresar;
    ImageView imgfotodeproducto;
    Intent tomarfotointent;
    String urldefoto;
    String idproducto, accion = "nuevo";
    Button btnagregarproducto;
    DB miconexion;
    TextView temp;


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


        String datos[] = {idproducto,codigo,descripcion,marca,presentacion,presio,urldefoto};
        miconexion.administracion_de_productos(accion,datos);
        mensajes("Registro guardado");
        regresarmainactivity();
        
        mostrardatosproducto();
    }

    private void mostrardatosproducto() {

           try {
               Bundle recibirparametros = getIntent().getExtras();
               accion = recibirparametros.getString("accion");
               if(accion.equals("modificar")){
                   String[] datos = recibirparametros.getStringArray("datos");

                   idproducto = datos[0];

                   temp = findViewById(R.id.txtcodigo);
                   temp.setText(datos[1]);

                   temp = findViewById(R.id.txtdescripcion);
                   temp.setText(datos[2]);

                   temp = findViewById(R.id.txtmarca);
                   temp.setText(datos[3]);

                   temp = findViewById(R.id.txtpresentacion);
                   temp.setText(datos[4]);

                   temp = findViewById(R.id.txtprecio);
                   temp.setText(datos[5]);

                   urldefoto = datos[6];
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