package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

public class vistaPeli extends AppCompatActivity {

    FloatingActionButton btnregresar;
    ImageView imgfotodepeli;
    VideoView vdidepeli;
    String urldefoto, urldevideo,idpeli,idlocal, accion = "nuevo", rev;
    TextView temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_peli);


        vdidepeli = findViewById(R.id.vdipelicula);
        imgfotodepeli = findViewById(R.id.imgfotopelicula);
        btnregresar = findViewById(R.id.btnregresar);
        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

try {
    mostrardatos();
    controles();
}catch (Exception e){

}
    }


    private void mostrardatos() {
        try {
            Bundle recibirparametros = getIntent().getExtras();
            accion = recibirparametros.getString("accion");
            idlocal = recibirparametros.getString("idlocal");
            if(accion.equals("ver")){
                JSONObject datos = new JSONObject(recibirparametros.getString("datos")).getJSONObject("value");
                idpeli = datos.getString("_id");

                rev = datos.getString("_rev");

                temp = findViewById(R.id.txtTitulo);
                temp.setText(datos.getString("titulo"));

                temp = findViewById(R.id.txtsinopsis);
                temp.setText(datos.getString("sinopsis"));

                temp = findViewById(R.id.txtduracion);
                temp.setText(datos.getString("duracion"));

                temp = findViewById(R.id.txtprecio);
                temp.setText(datos.getString("precio"));

                urldefoto =  datos.getString("urlfoto");
                urldevideo =  datos.getString("urltriler");

                imgfotodepeli.setImageURI(Uri.parse(urldefoto));
                vdidepeli.setVideoURI(Uri.parse(urldevideo));

            }
        }catch (Exception ex){

        }
    }


    private void regresarmainactivity() {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    private void controles(){
        //Controles de video
        MediaController mediaController = new MediaController(this);
        vdidepeli.setMediaController(mediaController);
        mediaController.setAnchorView(vdidepeli);
    }

}