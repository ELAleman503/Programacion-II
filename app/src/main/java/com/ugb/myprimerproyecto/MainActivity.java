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
    String dui,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        di = new detectarInternet(getApplicationContext());
        login = findViewById(R.id.btniniciar);
        registro = findViewById(R.id.btnregistrar);

        login.setOnClickListener(v->{
            loginlocal();
        });

        registro.setOnClickListener(v->{
           Intent i = new Intent(getApplicationContext(), registrousuario.class);
           startActivity(i);
        });
    }
    private void loginlocal() {
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

                            Bundle parametros = new Bundle();
                            parametros.putString("nombre", nombre);
                            parametros.putString("duii", dui1);
                            parametros.putString("telefono", telefono);
                            parametros.putString("mail", mail);
                            parametros.putString("padss", padss);
                            Intent lanzar = new Intent(getApplicationContext(), mostrarvoluntarios.class);
                            lanzar.putExtras(parametros);
                            startActivity(lanzar);
                            try { String[] datos = {dui1,nombre, dui1, telefono, mail, padss};
                                miconexion.agregar_usuario("nuevo", datos);

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