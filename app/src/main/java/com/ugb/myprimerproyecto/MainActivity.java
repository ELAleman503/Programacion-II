package com.ugb.myprimerproyecto;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button login, registro;
    TextView temp;
    DB miconex;
    JSONArray jsonArrayDatosvo;
    JSONObject jsonObjectDatosvo;
    utilidades uti;
    detectarInternet detectarInterneti;
    Cursor datosusuariocursor = null;
    String dui,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        detectarInterneti = new detectarInternet(getApplicationContext());
        login = findViewById(R.id.btniniciar);
        registro = findViewById(R.id.btnregistrar);

        login.setOnClickListener(v->{
            logilocal();
        });

        registro.setOnClickListener(v->{
           Intent i = new Intent(getApplicationContext(), registroUSS.class);
           startActivity(i);
        });


    }

    private void logilocal() {
        temp = findViewById(R.id.txtuss);
        dui = temp.getText().toString();

        temp = findViewById(R.id.txtpass);
        pass = temp.getText().toString();
        logi();


    }

    private void logi() {
     try {

         ConexionconServer conexionconServer = new ConexionconServer();
         String resp = conexionconServer.execute(uti.urlobteniendodatos_US, "GET").get();
         jsonObjectDatosvo=new JSONObject(resp);
         jsonArrayDatosvo = jsonObjectDatosvo.getJSONArray("rows");


         JSONObject jsonObject;
         if(detectarInterneti.hayConexionInternet()) {
             if(jsonArrayDatosvo.length()>0) {
                 mensajes("Espere un momento estoy buscando");
                 for (int i = 0; i < jsonArrayDatosvo.length(); i++) {

                     jsonObject = jsonArrayDatosvo.getJSONObject(i).getJSONObject("value");

                    if (dui.equalsIgnoreCase(jsonObject.getString("dui"))){

                        if (pass.equalsIgnoreCase(jsonObject.getString("pass"))){
                            i = jsonArrayDatosvo.length()+1;
                            String nombre = jsonObject.getString("nombre");
                            String duii = jsonObject.getString("dui");
                            String telefono = jsonObject.getString("telefono");
                            String mail = jsonObject.getString("correo");
                            String padss = jsonObject.getString("pass");


                            Bundle parametros = new Bundle();
                            parametros.putString("nombre", nombre);
                            parametros.putString("duii", duii);
                            parametros.putString("telefono", telefono);
                            parametros.putString("mail", mail);
                            parametros.putString("padss", padss);
                            Intent lanzar = new Intent(getApplicationContext(), mostrarvolunt.class);
                            lanzar.putExtras(parametros);
                            startActivity(lanzar);

                            try { String[] datos = {duii,nombre, duii, telefono, mail, padss};
                                miconex.agregar_usuario("nuevo", datos);

                            }catch (Exception e){}

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