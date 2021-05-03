package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;
//Diego Alexander Alemán Castro        USIS012120
//Mauricio Adonay Espinoza Villatoro   USIS018120
//Mariano José Ortega Hernández        USIS016820
//Kevin Aníbal Campos jurado           USIS017320
public class agregarpeliculas extends AppCompatActivity {


    FloatingActionButton btnregresar;
    ImageView imgfotodepeli;
    VideoView vdidepeli;
    String urldefoto="", urldevideo="",idpeli,idlocal, accion = "nuevo", rev, urivideo;
    Button btnagregar, btncargarvideo;
    DB miconexion;
    TextView temp;
    utilidades miUrl;
    detectarInternet di;
    private static final int RPQ= 100;
    private static final int RIG= 101;
    private static final int RVD= 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarpelis);

        miconexion = new DB(getApplicationContext(),"",null,1);
        btnregresar = findViewById(R.id.btnregresar);
        btncargarvideo = findViewById(R.id.btncargarvideo);
        imgfotodepeli = findViewById(R.id.imgfotopelicula);
        vdidepeli = findViewById(R.id.vdipelicula);
        btnagregar = findViewById(R.id.btnguardarpelicula);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        imgfotodepeli.setOnClickListener(v -> {
            abrirgaleriaimagen();
        });

        btncargarvideo.setOnClickListener(v -> {
            abrirgaleriavideo();
        });

        btnagregar.setOnClickListener(v -> {
           agregar();
        });

            permisos();
            mostrardatos();
            controles();
    }


    private void agregar() {
        try {
            temp = findViewById(R.id.txtTitulo);
            String titulo = temp.getText().toString();

            temp = findViewById(R.id.txtsinopsis);
            String sinopsis = temp.getText().toString();

            temp = findViewById(R.id.txtduracion);
            String duracion = temp.getText().toString();

            temp = findViewById(R.id.txtCosto);
            String precio = temp.getText().toString();

            JSONObject datospelis = new JSONObject();
            if(accion.equals("modificar") && idpeli.length()>0 && rev.length()>0 ){
                datospelis.put("_id",idpeli);
                datospelis.put("_rev",rev);
            }



            datospelis.put("titulo",titulo);
            datospelis.put("sinopsis",sinopsis);
            datospelis.put("duracion",duracion);
            datospelis.put("precio",precio);
            datospelis.put("urlfoto",urldefoto);
            datospelis.put("urltriler",urldevideo);


            String[] datos = {idlocal, titulo, sinopsis, duracion, precio, urldefoto, urldevideo };
            miconexion.administracion_de_pelis(accion, datos);

            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) {
                enviarDatos guardarpelis = new enviarDatos(getApplicationContext());
                String resp = guardarpelis.execute(datospelis.toString()).get();
            }

            mensajes("Registro guardado con exito.");
            regresarmainactivity();
        }catch (Exception w){
            mensajes(w.getMessage());
        }
    }

    private void mostrardatos() {
        try {
            Bundle recibirparametros = getIntent().getExtras();
            accion = recibirparametros.getString("accion");
            idlocal = recibirparametros.getString("idlocal");
            if(accion.equals("modificar")){
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

                // Bitmap bitmap = BitmapFactory.decodeFile(urldefoto);
                //     imgfotodeproducto.setImageBitmap(bitmap);
            }
        }catch (Exception ex){
            mensajes(ex.getMessage());

        }
    }

    private void permisos() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        if (ActivityCompat.checkSelfPermission(agregarpeliculas.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
         }else {
            ActivityCompat.requestPermissions(agregarpeliculas.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RPQ);
        }
    }else {
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent dataimagen) {
            if (resultCode == Activity.RESULT_OK && dataimagen != null) {
                if (requestCode == RIG) {
                Uri photo = dataimagen.getData();
                imgfotodepeli.setImageURI(photo);

                urldefoto = getRealUrl(this,photo);


            }else if (requestCode == RVD){
                    Uri video = dataimagen.getData();
                    vdidepeli.setVideoURI(video);

                    urldevideo = getRealUrl(this,video);
                }
        }
        super.onActivityResult(requestCode, resultCode, dataimagen);
    }

    private void abrirgaleriaimagen(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT );
        i.setType("image/*");
        startActivityForResult(i, RIG);
    }

    private void abrirgaleriavideo(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT );
        i.setType("video/*");
        startActivityForResult(i, RVD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode== RPQ){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }else{
                mensajes("Por favor dame los permisos");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }



//las url de android jellybeans en adelante contienen permisos temporales para acceder a ellas
    //con este metodo se octiene la url real del archivo para evitar probleamas
    public static String getRealUrl(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}