package com.ugb.myprimerproyecto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class agregarpostulados extends AppCompatActivity {
    FloatingActionButton btnregresar;
    ImageView imgfoto;
    VideoView vdidep;
    String urldefoto="", urldevideo="",idpostulado, accion = "nuevo", rev;
    Button btnagregar, btncargarvideo;
    TextView temp;
    detectarInternet di;
    private static final int RPQ= 100;
    private static final int RIG= 101;
    private static final int RVD= 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarpostulados);
        btnregresar = findViewById(R.id.btnregresar);
        btncargarvideo = findViewById(R.id.btncargarvideo);
        imgfoto = findViewById(R.id.imgfoto);
        vdidep = findViewById(R.id.vdip);
        btnagregar = findViewById(R.id.btnguardarpelicula);

        btnregresar.setOnClickListener(v -> {
            regresarmainactivity();
        });

        imgfoto.setOnClickListener(v -> {
            abrirgaleriaimagen();
        });

        btncargarvideo.setOnClickListener(v -> {
            abrirgaleriavideo();
        });

        btnagregar.setOnClickListener(v -> {
            agregar();
        });

        permisos();
      //  mostrardatos();
        controles();
    }

    private void agregar() {
        try {
            temp = findViewById(R.id.txtnombre);
            String nombre = temp.getText().toString();

            temp = findViewById(R.id.txtdui);
            String dui = temp.getText().toString();

            temp = findViewById(R.id.txtpropuesta);
            String propuesta = temp.getText().toString();

            temp = findViewById(R.id.txtotro);
            String otro = temp.getText().toString();

            JSONObject datoss = new JSONObject();
            if(accion.equals("modificar") && idpostulado.length()>0 && rev.length()>0 ){
                datoss.put("_id",idpostulado);
                datoss.put("_rev",rev);
            }

            datoss.put("nombre",nombre);
            datoss.put("dui",dui);
            datoss.put("propuesta",propuesta);
            datoss.put("otro",otro);
            datoss.put("urlfoto",urldefoto);
            datoss.put("urltriler",urldevideo);

            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) {
                enviarDatos guardarpelis = new enviarDatos(getApplicationContext());
                String resp = guardarpelis.execute(datoss.toString()).get();
            }

            mensajes("Registro guardado con exito.");
            regresarmainactivity();
        }catch (Exception w){
            mensajes(w.getMessage());
        }
    }

    private void permisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(agregarpostulados.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            }else {
                ActivityCompat.requestPermissions(agregarpostulados.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RPQ);
            }
        }else {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent dataimagen) {
        if (resultCode == Activity.RESULT_OK && dataimagen != null) {
            if (requestCode == RIG) {
                Uri photo = dataimagen.getData();
                imgfoto.setImageURI(photo);

                urldefoto = getRealUrl(this,photo);


            }else if (requestCode == RVD){
                Uri video = dataimagen.getData();
                vdidep.setVideoURI(video);

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
        Intent i = new Intent(getApplicationContext(),mostrarpostulados.class);
        startActivity(i);
    }

    private void controles(){
        //Controles de video
        MediaController mediaController = new MediaController(this);
        vdidep.setMediaController(mediaController);
        mediaController.setAnchorView(vdidep);
    }


    private void mensajes(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }



    //las url de android jellybeans en adelante contienen permisos temporales para acceder a ellas
    //con este metodo se octiene la url real del archivo para evitar probleamas
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
