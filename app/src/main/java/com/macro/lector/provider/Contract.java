package com.macro.lector.provider;

import android.net.Uri;
import android.provider.BaseColumns;

import com.macro.lector.util.Constantes;

public class Contract {
    public static final Uri URI_BASE = Uri.parse("content://" + Constantes.AUTHORITY);

    private static final String RUTA_DATOS = "datos";
    private static final String RUTA_FICHAS = "fichas";
    private static final String RUTA_SERIES = "series";

    public interface ColumnasDato extends BaseColumns {
        String ID_DATO = "id_dato";
        String DESCRIPCION = "descripcion";
    }

    public interface ColumnasFicha extends BaseColumns {
        String ID_FICHA = "id_ficha";
        String DESCRIPCION = "descripcion";
    }

    public interface ColumnasSerie extends BaseColumns {
        String ID_SERIE = "id_serie";
        String DESCRIPCION = "descripcion";
    }

    ///////////////////////////////////////////////////////
    ////////////////Fin de las Interfaces//////////////////
    ///////////////////////////////////////////////////////


    public static class Datos implements ColumnasDato {
        public static final String[] PROJECTION = new String[]{
                ColumnasDato.ID_DATO,
                ColumnasDato.DESCRIPCION,
        };

        public static final int COLUMNA_ID_DATO = 0;
        public static final int COLUMNA_DESCRIPCION = 1;

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_DATOS).build();
    }

    public static class Fichas implements ColumnasFicha {
        public static final String[] PROJECTION = new String[]{
                ColumnasFicha.ID_FICHA,
                ColumnasFicha.DESCRIPCION,
        };

        public static final int COLUMNA_ID_FICHA = 0;
        public static final int COLUMNA_DESCRIPCION = 1;

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_FICHAS).build();
    }

    public static class Series implements ColumnasSerie {
        public static final String[] PROJECTION = new String[]{
                ColumnasSerie.ID_SERIE,
                ColumnasSerie.DESCRIPCION,
        };

        public static final int COLUMNA_ID_SERIE = 0;
        public static final int COLUMNA_DESCRIPCION = 1;

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_SERIES).build();
    }

}
