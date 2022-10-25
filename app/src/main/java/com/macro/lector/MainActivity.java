package com.macro.lector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {
    Button btnDatos, btnSeries, btnFichas;
    private long tiempoPrimerClick;
    private static final int INTERVALO = 2000; //2 segundos para salir
    //EdwinP. path para localizar el directorio y seleccionar archivos
    private String stringFile =  Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatos = findViewById(R.id.btnDatos);
        btnSeries = findViewById(R.id.btnSeries);
        btnFichas = findViewById(R.id.btnFichas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Scan.class);
                intent.putExtra("file","Datos");
                startActivity(intent);
            }
        });

        btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), Scan.class);
                Intent intent = new Intent(getApplicationContext(), Scan.class);
                intent.putExtra("file","Series");
                startActivity(intent);
            }
        });

        btnFichas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Scan.class);
                intent.putExtra("file","Fichas");
                startActivity(intent);
            }
        });

        //EdwinP. se le da acción al FAB
        FloatingActionButton fab = findViewById(R.id.fabSearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir el dialog explorer

            }
        });
        }

        //EdwinP, se integra para activar el toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
        //EdwinP. se integra para ejecutar las acciones al seleccionar opciones
        //Intentando ejecutar el File Explorer para seleccionar archivo(s) y compartir
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.opc1) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse("/Docs");
            intent.setDataAndType(uri,"*/*");
            startActivity(Intent.createChooser(intent, "Open"));
        }
        return true;
        }

    @Override
    public void onBackPressed(){ //EdwinP. Se coloca para notificar al usuario que debe presionar dos veces el backbutton para salir de la app
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }
}
