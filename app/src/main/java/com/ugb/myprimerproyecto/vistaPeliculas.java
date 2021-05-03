package com.ugb.myprimerproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
//Diego Alexander Alemán Castro        USIS012120
//Mauricio Adonay Espinoza Villatoro   USIS018120
//Mariano José Ortega Hernández        USIS016820
//Kevin Aníbal Campos jurado           USIS017320
public class vistaPeliculas extends AppCompatActivity {

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

                temp = findViewById(R.id.txtCosto);
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
        MediaController mediaController = new MediaController(this);
        vdidepeli.setMediaController(mediaController);
        mediaController.setAnchorView(vdidepeli);
    }

}