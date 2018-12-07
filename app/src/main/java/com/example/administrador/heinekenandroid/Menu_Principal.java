package com.example.administrador.heinekenandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.administrador.heinekenandroid.Extras.Descarga_Informacion;
import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Delete;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;

import org.json.JSONObject;

public class Menu_Principal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progress;
    Delete delete;
    Insert insert;

    JSONObject mJsonObject;
    PK pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        delete = new Delete(getApplicationContext());
        insert = new Insert(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        setTitle("BIENVENIDO: " + pk.nombre);

        /*if(getIntent().hasExtra("json")) {
            try {
                mJsonObject = new JSONObject(getIntent().getStringExtra("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Principal.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(Menu_Principal.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_entradas) {
            Intent intent = new Intent(getApplicationContext(), Inputs.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        } else if (id == R.id.nav_salidas) {
            Intent intent = new Intent(getApplicationContext(), Outputs.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        } else if (id == R.id.nav_inventarios) {
            Intent intent = new Intent(getApplicationContext(), Inventory.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        } else if (id == R.id.nav_salida_ruta) {
            Intent intent = new Intent(getApplicationContext(), Route.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        }else if (id == R.id.nav_ruta_detalle) {
            Intent intent = new Intent(getApplicationContext(), Route_Detail.class);
            intent.putExtra("pk", pk);
            intent.putExtra("ubicacion", "menu");
            startActivity(intent);
        } else if (id == R.id.nav_devoluciones) {
            Intent intent = new Intent(getApplicationContext(), Returns.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        } else if (id == R.id.nav_sincronizar_catalogos) {
            delete.deleteCatalogosFile();
            Descarga_Informacion descargarInfo = new Descarga_Informacion(getApplicationContext(), Menu_Principal.this);
                descargarInfo.Descargar_Catalogo(pk, "menu");
        } else if (id == R.id.nav_configuracion) {
            Intent intent = new Intent(getApplicationContext(), Configuracion.class);
            intent.putExtra("pk", pk);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
