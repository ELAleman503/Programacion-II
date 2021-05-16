package com.ugb.myprimerproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button login, registro;
    TextView temp;
    DB miconexion;
    utilidades u;
    JSONArray jsonArrayDatosvotantes;
    JSONObject jsonObjectDatosvotantes;
    Cursor datosusuariocursor = null;
    detectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        di = new detectarInternet(getApplicationContext());
        login = findViewById(R.id.btniniciar);
        registro = findViewById(R.id.btnregistrar);

        login.setOnClickListener(v->{
            logi();
        });

        registro.setOnClickListener(v->{
           Intent i = new Intent(getApplicationContext(), registrorvotante.class);
           startActivity(i);
        });
    }

    private void logi() {
     try {
         temp = findViewById(R.id.txtuss);
         String dui = temp.getText().toString();

         temp = findViewById(R.id.txtpass);
         String pass = temp.getText().toString();

         ConexionconServer conexionconServer = new ConexionconServer();
         String resp = conexionconServer.execute(u.urlobtenerdatosvotantes, "GET").get();
         jsonObjectDatosvotantes=new JSONObject(resp);
         jsonArrayDatosvotantes = jsonObjectDatosvotantes.getJSONArray("rows");


         JSONObject jsonObject;
         if(di.hayConexionInternet()) {
             if(jsonArrayDatosvotantes.length()>0) {
                 mensajes("Buscando");
                 for (int i = 0; i < jsonArrayDatosvotantes.length(); i++) {

                     jsonObject = jsonArrayDatosvotantes.getJSONObject(i).getJSONObject("value");

                    if (dui.equalsIgnoreCase(jsonObject.getString("dui"))){

                        if (pass.equalsIgnoreCase(jsonObject.getString("pass"))){
                            i = jsonArrayDatosvotantes.length()+1;
                            String nombre = jsonObject.getString("nombre");
                            String duii = jsonObject.getString("dui");
                            String telefono = jsonObject.getString("telefono");
                            String mail = jsonObject.getString("correo");
                            String padss = jsonObject.getString("pass");

                            mensajes("Bienvenido " + nombre);
                            Intent ik = new Intent(MainActivity.this, mostrarpostulados.class);
                            ik.putExtra(mostrarpostulados.nombre, nombre);
                            ik.putExtra(mostrarpostulados.duii,duii);
                            ik.putExtra(mostrarpostulados.telefono, telefono);
                            ik.putExtra(mostrarpostulados.mail, mail);
                            ik.putExtra(mostrarpostulados.padss,padss);
                            startActivity(ik);
                        }
                     }
                 }}
         }

     }catch (Exception e){
         mensajes(e.getMessage());
     }
    }


    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
}