package com.macro.lector;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.macro.lector.Entidades.Detalle;

import java.io.File;
import java.io.FileWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;
//import java.util.Comparator;

public class Scan extends AppCompatActivity {
    private EditText etReader;
    private TextView tvCount;
    private InputMethodManager imm;
    private String fileName = "";
    private String pathFinal="";
    //private int count = 0;
    private int bandera=0;

    //private String MediaStore;
//    private String stringFile =  Environment.getExternalStorageDirectory().getPath();
    String path = Environment.getExternalStorageDirectory()+"/Docs/Series.txt";

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    private Detalle detalle;
    private AdaptadorDeDetalles adapter;
    ArrayList<Detalle> contacts = new ArrayList<>();
    ArrayList<Detalle> contactsOrdenados = new ArrayList<>();
    /////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intentRes;
        intentRes = getIntent();
        fileName = intentRes.getStringExtra("file"); //Nombre del archivo

        etReader = findViewById(R.id.etReader);
        tvCount = findViewById(R.id.tvCount);

        Button btGenerar = findViewById(R.id.btnGenerar);
        imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        etReader.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                detalle = new Detalle();
                detalle.setId_visita(contacts.size());
                detalle.setDescripcion(etReader.getText().toString());
                contacts.add(detalle);

//                    adapter.notifyData(contacts);
                cargarResultados();

                etReader.setText("");
                tvCount.setText("" + contacts.size());

//                    etReader.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            etReader.setFocusableInTouchMode(true);
//                            etReader.requestFocus();
//                            hideSoftKeyboard(v);
//                        }
//                    });
                etReader.postDelayed(() -> {
                    etReader.setFocusableInTouchMode(true);
                    etReader.requestFocus();
                    hideSoftKeyboard(v);
                },100);


                return false;
            }
            return false;
        });

        btGenerar.setOnClickListener(v -> {
            if (contacts.size()!=0)
            _generararchivo();
            else
            {
                Toast.makeText(this,"debe scañar un o mas para poder guardar el archivo",Toast.LENGTH_SHORT).show();
            }

        });

        /////////////////////////////////////////////////////////////
        RecyclerView recyclerView = findViewById(R.id.rvResumen);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        adapter = new AdaptadorDeDetalles(getApplicationContext());
        adapter = new AdaptadorDeDetalles(getApplicationContext(), position -> {
//                Toast.makeText(getApplicationContext(),"" + position, Toast.LENGTH_SHORT).show();
            contacts.remove(position);
//                adapter.notifyData(contacts);
            cargarResultados();
            tvCount.setText("" + contacts.size());
        });

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(Scan.this, DividerItemDecoration.VERTICAL));

        //EdwinP. líneas para darle acción al botón compartir
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public void hideSoftKeyboard(View view){
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //EdwinP. Se comentó estas líneas para que el retroceso al home de opciones, sea solo presionando el back button
//    @Override
//    public void onBackPressed(){
//        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
//            super.onBackPressed();
//            return;
//        }else {
//            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
//        }
//        tiempoPrimerClick = System.currentTimeMillis();
//    }

    @Override
    public void onResume() {
        super.onResume();
        etReader.clearFocus();
        etReader.requestFocus();
    }


    public void cargarResultados() {
        contactsOrdenados = new ArrayList<>();

        //fecha_final asc
        Collections.sort(contacts, (o1, o2) -> {
            Integer id1 = o1.getId_visita();
            Integer id2 = o2.getId_visita();
            return id2.compareTo(id1);
        });

        adapter.notifyData(contacts);
    }

    public void buttonShareFile(View view){
            File file = new File(path);

            if (!file.exists()){
                Toast.makeText(this, "El archivo no existe", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intentShare = new Intent(Intent.ACTION_SEND);
            intentShare.setType("*/*");
            intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
            startActivity(Intent.createChooser(intentShare,"Compartir en"));
    }
    private void _generararchivo(){
        try {
            //EdwinP. se establece el directorio por defecto para guardar los archivos txt
            //String path = Environment.getExternalStorageDirectory() + "/Android/media/com.macro.lector/Docs/";
            String path = Environment.getExternalStorageDirectory() + "/Docs/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                Calendar calendar= Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dMy");
                String date= dateFormat.format(Calendar.getInstance().getTime());
                LocalDateTime locaDate = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    locaDate = LocalDateTime.now();
                }
                int hours  = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    hours = locaDate.getHour();
                }
                int minutes = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    minutes = locaDate.getMinute();
                }
                int seconds = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    seconds = locaDate.getSecond();
                }
                String _hours=String.valueOf(hours)+":";
                String _minutes=String.valueOf(minutes)+":";
                String horalocal="";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    horalocal= String.valueOf(LocalTime.now());
                }
                if ( bandera == 0){
                    String fullName = path + fileName +"-"+ date+hours+minutes+seconds+".txt";
                    pathFinal=fullName;
                    bandera=1;
                }

                //  String fullName = path + fileName +"-"+ date + horalocal +".txt";
                File file = new File(pathFinal);
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
//                        writer.append(tvResult.getText());
                for(int ii = 0; ii < contacts.size(); ii++){
                    if (ii > 0){
                        writer.append("\r\n");
                    }
                    writer.append(contacts.get(ii).descripcion);
                }
                writer.flush();
                writer.close();

            } catch (Exception e) {
                Log.e("Error", "exFile: " + e);
            }

        } catch (Exception e) {
            Log.e("Error", "exDir: " + e);
        }

        Toast.makeText(getApplicationContext(), "El archivo " + fileName + ".txt se ha generado correctamente.", Toast.LENGTH_SHORT).show();
    }
}
