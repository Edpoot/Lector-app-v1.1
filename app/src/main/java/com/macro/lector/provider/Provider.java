package com.macro.lector.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.macro.lector.util.Constantes;

import static com.macro.lector.util.Constantes.DATABASE_NAME;

public class Provider extends ContentProvider {
    private static final int DATABASE_VERSION = 1;
    private ContentResolver resolver;
    private DataBase dataBaseHelper;

    public static final UriMatcher uriMatcher;
    public static final int DATOS = 1;
    public static final int FICHAS = 2;
    public static final int SERIES = 3;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constantes.AUTHORITY, "datos", DATOS);
        uriMatcher.addURI(Constantes.AUTHORITY, "fichas", FICHAS);
        uriMatcher.addURI(Constantes.AUTHORITY, "series", SERIES);
    }

    @Override
    public boolean onCreate() {
//        return false;
        dataBaseHelper = new DataBase(getContext(), DATABASE_NAME, null, DATABASE_VERSION);

        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
//        return null;
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Cursor c;
        System.out.println("QUERY: " + uri);

        switch (match) {
            case DATOS:
                c = db.query(DataBase.Tablas.DATO, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        Contract.Datos.URI_CONTENIDO);

                break;

            case FICHAS:
                c = db.query(DataBase.Tablas.FICHA, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        Contract.Fichas.URI_CONTENIDO);
                break;

            case SERIES:
                c = db.query(DataBase.Tablas.SERIE, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                c.setNotificationUri(
                        resolver,
                        Contract.Series.URI_CONTENIDO);
                break;

            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }

        System.out.println(DatabaseUtils.dumpCursorToString(c));
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        return null;
        long rowId = 0;
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        switch (uriMatcher.match(uri)) {
            case DATOS:
                rowId = db.insert(DataBase.Tablas.DATO, null, contentValues);
                if (rowId > 0) {
                    Uri uri_dato = ContentUris.withAppendedId(
                            Contract.Datos.URI_CONTENIDO, rowId);
                    resolver.notifyChange(uri_dato, null, false);
                    resolver.notifyChange(Contract.Datos.URI_CONTENIDO, null, false);
                    return uri_dato;
                }

            case FICHAS:
                rowId = db.insert(DataBase.Tablas.FICHA, null, contentValues);
                if (rowId > 0) {
                    Uri uri_ficha = ContentUris.withAppendedId(
                            Contract.Fichas.URI_CONTENIDO, rowId);
                    resolver.notifyChange(uri_ficha, null, false);
                    resolver.notifyChange(Contract.Fichas.URI_CONTENIDO, null, false);
                    return uri_ficha;
                }

            case SERIES:
                rowId = db.insert(DataBase.Tablas.SERIE, null, contentValues);
                if (rowId > 0) {
                    Uri uri_serie = ContentUris.withAppendedId(
                            Contract.Series.URI_CONTENIDO, rowId);
                    resolver.notifyChange(uri_serie, null, false);
                    resolver.notifyChange(Contract.Series.URI_CONTENIDO, null, false);
                    return uri_serie;
                }
            default:
                throw new UnsupportedOperationException("Uri no soportada");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        return 0;
        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
        long rowId = 0;
        int match = uriMatcher.match(uri);
        int affected = 0;

        switch (match) {
            case DATOS:
//                affected = db.delete(DataBaseHelper.Tablas.IMAGENASIGNACION,
////
//                        (!TextUtils.isEmpty(selection) ? selection :
//                                ContractADMKT.ColumnasImagenAsignacion.ID_ASIGNACION + "=" + rowId)
//
//
////                        ContractPedido.ColumnasDetallePedido.ID_REMOTA + "=" + rowId
////                                + (!TextUtils.isEmpty(selection) ?
////                                " AND (" + selection + ')' : "")
//                        ,
//                        selectionArgs);
//
//

                affected = db.delete(DataBase.Tablas.DATO,
                        selection, selectionArgs);

                resolver.notifyChange(uri, null, false);
                break;
        }

        return affected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
