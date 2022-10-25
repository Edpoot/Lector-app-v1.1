package com.macro.lector.provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.macro.lector.util.Constantes;

public class DataBase extends SQLiteOpenHelper {
    interface Tablas {
        String DATO = "dato";
        String FICHA = "ficha";
        String SERIE = "serie";
    }

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    private void createTable(SQLiteDatabase database) {
        String cmdDato = "CREATE TABLE IF NOT EXISTS " + Tablas.DATO + " (" +
                Contract.ColumnasDato.ID_DATO + " INTEGER PRIMARY KEY, " +
                Contract.ColumnasDato.DESCRIPCION + " TEXT )";
        database.execSQL(cmdDato);

        String cmdFicha = "CREATE TABLE IF NOT EXISTS " + Tablas.FICHA + " (" +
                Contract.ColumnasFicha.ID_FICHA + " INTEGER PRIMARY KEY, " +
                Contract.ColumnasFicha.DESCRIPCION + " TEXT )";
        database.execSQL(cmdDato);

        String cmdSerie = "CREATE TABLE IF NOT EXISTS " + Tablas.SERIE + " (" +
                Contract.ColumnasSerie.ID_SERIE + " INTEGER PRIMARY KEY, " +
                Contract.ColumnasSerie.DESCRIPCION + " TEXT )";
        database.execSQL(cmdDato);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("drop table " + Tablas.DATO);
            db.execSQL("drop table " + Tablas.FICHA);
            db.execSQL("drop table " + Tablas.SERIE);
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        onCreate(db);
    }

    public void removeDatabase(Context mContext) {
        mContext.deleteDatabase(Constantes.DATABASE_NAME);
//        LOGD("Database Deleted...");
    }

}
