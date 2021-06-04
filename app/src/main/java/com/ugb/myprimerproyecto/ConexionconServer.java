package com.ugb.myprimerproyecto;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionconServer extends AsyncTask<String, String, String> {
    HttpURLConnection urlConnection;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... parametros) {
        StringBuilder result = new StringBuilder();
        try{
            String uri = parametros[0];
            String metodo = parametros[1];
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod(metodo);

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;
            while( (linea=bufferedReader.readLine())!=null ){
                result.append(linea);
            }
        }catch (Exception e){
            Log.i("GET", e.getMessage());
        }
        return result.toString();
    }
}
