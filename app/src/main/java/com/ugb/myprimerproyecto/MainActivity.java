package com.ugb.myprimerproyecto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.an.biometric.BiometricCallback;
import com.an.biometric.BiometricManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements BiometricCallback {

    Button login, registro;
    TextView temp;
    DB miconexion;
    utilidades u;
    JSONArray jsonArrayDatosvo;
    JSONObject jsonObjectDatosvo;
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
            logilocal();
        });

        registro.setOnClickListener(v->{
           Intent i = new Intent(getApplicationContext(), registrorvolunt.class);
           startActivity(i);
        });

        new BiometricManager.BiometricBuilder(MainActivity.this)
                .setTitle("DonacioneSV")
                .setSubtitle("Identificate")
                .setDescription("Por favor coloca tu huella en el sensor")
                .setNegativeButtonText("Cancelar")
                .build()
                .authenticate(MainActivity.this);
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
         String resp = conexionconServer.execute(u.urlobteniendodatos_US, "GET").get();
         jsonObjectDatosvo=new JSONObject(resp);
         jsonArrayDatosvo = jsonObjectDatosvo.getJSONArray("rows");


         JSONObject jsonObject;
         if(di.hayConexionInternet()) {
             if(jsonArrayDatosvo.length()>0) {
                 mensajes("Buscando "+dui);
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

                            //Bienvenida
                            mensajes("Bienvenido "+nombre );

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

    @Override
    public void onSdkVersionNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotSupported() {

    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {

    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {

    }

    @Override
    public void onBiometricAuthenticationInternalError(String error) {

    }

    @Override
    public void onAuthenticationFailed() {

    }

    @Override
    public void onAuthenticationCancelled() {

    }

    @Override
    public void onAuthenticationSuccessful() {


        try {
            miconexion = new DB(getApplicationContext(), "", null, 1);
            datosusuariocursor = miconexion.consultar_usuario("consultar");

            if( datosusuariocursor.moveToFirst() ){
              dui=   datosusuariocursor.getString(2);
              pass=   datosusuariocursor.getString(5);
                logi();
            }
        }catch (Exception e){
            mensajes(e.getMessage());
        }

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

    }
}