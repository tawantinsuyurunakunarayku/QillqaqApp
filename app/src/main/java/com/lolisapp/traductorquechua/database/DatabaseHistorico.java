package com.lolisapp.traductorquechua.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lolisapp.traductorquechua.bean.TraduccionHistorica;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 01/11/2017.
 */

public class DatabaseHistorico extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "HISTORICOTRADUCCIONDB";
    private static final String TABLE_TRADUCCION_HISTORICO="TABLE_HISTORICO";
    String sqlCreate = "CREATE TABLE ".concat(TABLE_TRADUCCION_HISTORICO).concat(" (ID INTEGER primary key autoincrement" +
            " ,ORIGINAL text ,TRADUCCION text , ISFAVORITE INTEGER)");


    public DatabaseHistorico(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS ".concat(TABLE_TRADUCCION_HISTORICO));
        db.execSQL(sqlCreate);
    }

    /*
    public List<TraduccionHistorica> getListTraduccionHistorica() {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" order by ID DESC")
            , null);

        if (cursor.moveToFirst()) {
            do {
                TraduccionHistorica traduccionHistorica = new TraduccionHistorica();
                traduccionHistorica.setId(cursor.getInt(0));
                traduccionHistorica.setOriginal(cursor.getString(1));
                traduccionHistorica.setTraduccion(cursor.getString(2));
                traduccionHistorica.setFavorite(cursor.getInt(3));
                lista.add(traduccionHistorica);

            } while (cursor.moveToNext());
        }


        return lista;

    }

*/


    /*
    public List<TraduccionHistorica> getListTraduccionHistoricaFavoritas() {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" where ISFAVORITE=1 order by ID DESC")
                , null);

        if (cursor.moveToFirst()) {
            do {
                TraduccionHistorica traduccionHistorica = new TraduccionHistorica();
                traduccionHistorica.setId(cursor.getInt(0));
                traduccionHistorica.setOriginal(cursor.getString(1));
                traduccionHistorica.setTraduccion(cursor.getString(2));
                traduccionHistorica.setFavorite(cursor.getInt(3));
                lista.add(traduccionHistorica);

            } while (cursor.moveToNext());
        }


        return lista;

    }

*/
    public List<TraduccionHistorica> getListTraduccionHistoricaFavoritasOrderText() {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" where ISFAVORITE=1 order by lower(TRADUCCION)")
                , null);

        if (cursor.moveToFirst()) {
            do {
                TraduccionHistorica traduccionHistorica = new TraduccionHistorica();
                traduccionHistorica.setId(cursor.getInt(0));
                traduccionHistorica.setOriginal(cursor.getString(1));
                traduccionHistorica.setTraduccion(cursor.getString(2));
                traduccionHistorica.setFavorite(cursor.getInt(3));
                lista.add(traduccionHistorica);

            } while (cursor.moveToNext());
        }


        return lista;

    }




    public List<TraduccionHistorica> getListTraduccionHistoricaFavoritasSearch(String query) {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" where ISFAVORITE=1 and ORIGINAL LIKE '%"+query+"%' order by ID DESC")
                , null);

        if (cursor.moveToFirst()) {
            do {
                TraduccionHistorica traduccionHistorica = new TraduccionHistorica();
                traduccionHistorica.setId(cursor.getInt(0));
                traduccionHistorica.setOriginal(cursor.getString(1));
                traduccionHistorica.setTraduccion(cursor.getString(2));
                traduccionHistorica.setFavorite(cursor.getInt(3));
                lista.add(traduccionHistorica);

            } while (cursor.moveToNext());
        }


        return lista;

    }


    public  void insertTraduccionHistorica(TraduccionHistorica traduccionHistorica){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("ORIGINAL", traduccionHistorica.getOriginal());
        nuevoRegistro.put("TRADUCCION",traduccionHistorica.getTraduccion());
        nuevoRegistro.put("ISFAVORITE",traduccionHistorica.getFavorite());

        db.insert(TABLE_TRADUCCION_HISTORICO, null, nuevoRegistro);
    }



    public void updateTraduccionHistorica(Integer id,Integer favorite){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("ISFAVORITE",favorite);
        db.update(TABLE_TRADUCCION_HISTORICO, valores, "ID=".concat(id.toString()), null);
    }

    public TraduccionHistorica getLastTraduccion() {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" order by ID DESC limit 1")
                , null);
        TraduccionHistorica traduccionHistorica = new TraduccionHistorica();

        if (cursor.moveToFirst()) {
                traduccionHistorica.setId(cursor.getInt(0));
                traduccionHistorica.setOriginal(cursor.getString(1));
                traduccionHistorica.setTraduccion(cursor.getString(2));
                traduccionHistorica.setFavorite(cursor.getInt(3));
                lista.add(traduccionHistorica);

        }


        return traduccionHistorica;

    }



    public TraduccionHistorica getTraduccion(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        List<TraduccionHistorica> lista = new ArrayList<>();

        Cursor cursor = db.rawQuery("select ID,ORIGINAL,TRADUCCION,ISFAVORITE from ".
                        concat(TABLE_TRADUCCION_HISTORICO).concat(" where ID="+id.toString()+" order by ID DESC limit 1")
                , null);
        TraduccionHistorica traduccionHistorica = new TraduccionHistorica();

        if (cursor.moveToFirst()) {
            traduccionHistorica.setId(cursor.getInt(0));
            traduccionHistorica.setOriginal(cursor.getString(1));
            traduccionHistorica.setTraduccion(cursor.getString(2));
            traduccionHistorica.setFavorite(cursor.getInt(3));
            lista.add(traduccionHistorica);

        }


        return traduccionHistorica;

    }

    public void deleteElement(Integer id){
         getWritableDatabase().delete(TABLE_TRADUCCION_HISTORICO, "ID=" + id.toString(), null) ;

    }

}
