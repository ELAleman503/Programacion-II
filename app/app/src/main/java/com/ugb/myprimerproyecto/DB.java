package com.ugb.myprimerproyecto;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    static String nombre_bd = "db_Tienda";
    static String tblproductos = "CREATE TABLE tblproductos(idproducto integer primary key autoincrement, codigo text, descripcion text, marca text, presentacion text, precio text, urlfoto text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre_bd, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblproductos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }



    public Cursor administracion_de_productos(String accion, String[] datos){
        Cursor datocursor = null;
        SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();

        switch (accion){
            case "consultar":
                datocursor = sqLiteDatabaseR.rawQuery("select * from tblproductos order by descripcion",null);
            break;

            case "nuevo":
                sqLiteDatabaseW.execSQL("INSERT INTO tblproductos(codigo, descripcion, marca, presentacion, precio, urlfoto) VALUES ('"+datos[1]+"','"+datos[2]+"','"+datos[3]+"','"+datos[4]+"','"+datos[5]+"','"+datos[6]+"')");
            break;
            case "modificar":
                sqLiteDatabaseW.execSQL("update tblproductos set codigo='"+datos[1]+"',descripcion='"+datos[2]+"',marca='"+datos[3]+"',presentacion='"+datos[4]+"',precio='"+datos[5]+"',urlfoto='"+datos[6]+"' where idproducto='"+datos[0]+"'");
                break;
            case "eliminar":
                sqLiteDatabaseW.execSQL("DELETE FROM tblproductos WHERE idproducto='"+ datos[0]+"'");
                break;
        }

   return datocursor; }
}
