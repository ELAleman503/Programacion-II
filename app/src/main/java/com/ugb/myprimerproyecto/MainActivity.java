package com.ugb.myprimerproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button login, registro;
    TextView temp;
    DB miconexion;
    utilidades uti;
    JSONArray jsonArrayDatosusuarios;
    JSONObject jsonObjectDatosusuarios;
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
           Intent i = new Intent(getApplicationContext(), registrousuario.class);
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
         String resp = conexionconServer.execute(uti.urlobteniendodatos_US, "GET").get();
         jsonObjectDatosusuarios=new JSONObject(resp);
         jsonArrayDatosusuarios = jsonObjectDatosusuarios.getJSONArray("rows");


         JSONObject jsonObject;
         if(di.hayConexionInternet()) {
             if(jsonArrayDatosusuarios.length()>0) {
                 mensajes("Buscar");
                 for (int i = 0; i < jsonArrayDatosusuarios.length(); i++) {
                     jsonObject = jsonArrayDatosusuarios.getJSONObject(i).getJSONObject("value");
                    if (dui.equalsIgnoreCase(jsonObject.getString("dui"))){

                        if (pass.equalsIgnoreCase(jsonObject.getString("pass"))){
                            i = jsonArrayDatosusuarios.length()+1;
                            String nombre = jsonObject.getString("nombre");
                            String dui1 = jsonObject.getString("dui");
                            String telefono = jsonObject.getString("telefono");
                            String mail = jsonObject.getString("correo");
                            String padss = jsonObject.getString("pass");

                            Intent ik = new Intent(MainActivity.this, mostrarvoluntarios.class);
                            ik.putExtra(mostrarvoluntarios.nombre, nombre);
                            ik.putExtra(mostrarvoluntarios.duii,dui1);
                            ik.putExtra(mostrarvoluntarios.telefono, telefono);
                            ik.putExtra(mostrarvoluntarios.mail, mail);
                            ik.putExtra(mostrarvoluntarios.padss,padss);
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