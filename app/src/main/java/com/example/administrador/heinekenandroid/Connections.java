package com.example.administrador.heinekenandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.example.administrador.heinekenandroid.dbQuerys.Update;
import com.heineken.greendaoapp.db.Propiedades;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_host;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_ruta;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Connections extends AppCompatActivity {

    PK pk;
    EditText editText_set_url;
    TextView textView_set_ruta;
    Button btn_guardar_connections;
    Update update;
    Select select;
    Insert insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");*/

        update = new Update(getApplicationContext());
        select = new Select(getApplicationContext());
        insert = new Insert(getApplicationContext());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.connectios_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_set_url = findViewById(R.id.editText_set_url);
        editText_set_url.setText(url_host);

        textView_set_ruta = findViewById(R.id.textView_set_ruta);
        textView_set_ruta.setText(url_ruta);

        btn_guardar_connections = findViewById(R.id.btn_guardar_connections);

        btn_guardar_connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_set_url.getText().toString().trim().replace(" ", "").isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Ingresar correctamente la URL.", Toast.LENGTH_SHORT);
                }
                else
                    {
                        Propiedades p = new Propiedades();
                        p.setId_url(1);
                        p.setUrl_server(editText_set_url.getText().toString().trim().replace(" ", ""));
                        if(select.getURL().isEmpty())
                        {
                            insert.insertURL(p);
                        }
                        else
                        {
                            update.updateURL(editText_set_url.getText().toString().trim().replace(" ", ""));
                        }

                        url_host = select.getURL();
                        url_server = (url_host + url_ruta);

                        /*Intent intent = new Intent(Connections.this, Configuracion.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("pk", pk);
                        startActivity(intent);*/
                        finish();
                    }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                /*Intent intent = new Intent(Connections.this, Configuracion.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pk", pk);
                startActivity(intent);*/
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Connections.this, Configuracion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);*/
        finish();
    }
}
