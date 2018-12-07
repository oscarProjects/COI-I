package com.example.administrador.heinekenandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.RegistrosAdapter;
import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Delete;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.heineken.greendaoapp.db.Rutas;
import com.heineken.greendaoapp.db.Salidas;
import com.heineken.greendaoapp.db.UM;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatter;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Route extends AppCompatActivity {

    FloatingActionButton fab;
    Spinner spinner_ruta_route, spinner_unidad_medida_route;
    EditText edittext_route_sku, edittext_descripcion_route, edittext_descripcion_cantidad;
    Button btn_guardar_route, btn_enviar_route, btn_consultar_route;
    List<Rutas> listCatRutas;
    List<UM> listCatUM;
    List<Salidas> listSalidas;
    List<String> listIdRutas, listIdUm;
    Select select; Insert insert; Delete delete;
    PK pk;
    String url_param, url_param_json, id_Rutas, desc_Rutas, id_Um, desc_Um, msg, cod_sku_final;

    Date date;
    RegistrosAdapter registrosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        select = new Select(getApplicationContext());
        insert = new Insert(getApplicationContext());
        delete = new Delete(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        m_findViewById();
        m_carga_Adapter_Rutas();
        m_carga_Adapter_Um();
        m_setListener();

    }

    private void m_findViewById()
    {
        fab = findViewById(R.id.fab);
        spinner_ruta_route = findViewById(R.id.spinner_ruta_route);
        spinner_unidad_medida_route = findViewById(R.id.spinner_unidad_medida_route);
        edittext_route_sku = findViewById(R.id.edittext_route_sku);
        edittext_descripcion_route = findViewById(R.id.edittext_descripcion_route);
        edittext_descripcion_cantidad = findViewById(R.id.edittext_descripcion_cantidad);
        btn_guardar_route = findViewById(R.id.btn_guardar_route);
        btn_enviar_route = findViewById(R.id.btn_enviar_route);
        btn_enviar_route.setVisibility(View.GONE);
        btn_consultar_route = findViewById(R.id.btn_consultar_route);
        btn_consultar_route.setVisibility(View.GONE);
    }

    private void m_carga_Adapter_Rutas()
    {
        listCatRutas = select.getRutas();
        Iterator<Rutas> iteratorS = listCatRutas.iterator();
        ArrayList<String> listR = new ArrayList<>();
        listIdRutas = new ArrayList<>();
        while (iteratorS.hasNext()) {
            Rutas aux = iteratorS.next();
            listR.add(aux.getRuta());
            listIdRutas.add(aux.getId_ruta());
        }
        ArrayAdapter<String> adaptadorS = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listR);
        adaptadorS.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_ruta_route.setAdapter(adaptadorS);
    }

    private void m_carga_Adapter_Um()
    {
        listCatUM = select.getUm();
        Iterator<UM> iteratorU = listCatUM.iterator();
        ArrayList<String> listU = new ArrayList<>();
        listIdUm = new ArrayList<>();
        while (iteratorU.hasNext()) {
            UM aux = iteratorU.next();
            listU.add(aux.getUm());
            listIdUm.add(aux.getId_um());
        }
        ArrayAdapter<String> adaptadorUM = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listU);
        adaptadorUM.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_unidad_medida_route.setAdapter(adaptadorUM);
    }

    private void m_setListener()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Route.this).initiateScan();
            }
        });

        edittext_route_sku.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //m_validation();
            }
        });

        edittext_route_sku.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    m_validation();
                    return true;
                }
                return false;
            }
        });

        spinner_ruta_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Rutas = String.valueOf(listIdRutas.get(position));
                desc_Rutas = spinner_ruta_route.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_unidad_medida_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Um = String.valueOf(listIdUm.get(position));
                desc_Um = spinner_unidad_medida_route.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_guardar_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validationSave() == true)
                {
                    date = new Date();
                    Salidas salidas = new Salidas();
                    salidas.set_id_(formatter.format(date));
                    salidas.setSku(cod_sku_final);
                    salidas.setIdum(id_Um);
                    salidas.setCantidad(edittext_descripcion_cantidad.getText().toString().trim());
                    salidas.setCodigo(edittext_route_sku.getText().toString().trim());
                    insert.insertSalidas(salidas);
                    spinner_ruta_route.setEnabled(false);
                    btn_enviar_route.setVisibility(View.VISIBLE);
                    btn_consultar_route.setVisibility(View.VISIBLE);
                    cleanElements();
                    Toast.makeText(getApplicationContext(), "Registro guardado correctamente.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_enviar_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarRegistros task = new GuardarRegistros();
                task.execute();
            }
        });

        btn_consultar_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert(desc_Rutas).show();
            }
        });
    }

    private void cleanElements()
    {
        edittext_route_sku.setText("");
        edittext_descripcion_route.setText("");
        edittext_descripcion_cantidad.setText("");
        //spinner_ruta_route.setSelection(0);
        spinner_unidad_medida_route.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                edittext_route_sku.setText(result.getContents());
                m_validation();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void m_validation()
    {
        try
        {
            String value = edittext_route_sku.getText().toString().trim();

            if (value.length() < 6)
            {
                Toast.makeText(this, "Codigo no valido.", Toast.LENGTH_SHORT).show();
            }
            else if (value.length() < 7)
            {
                m_consultas(value);
            }
            else if (value.length() < 17)
            {
                m_consultas(value.substring(0,6));
            }
            else if (value.length() > 23 && value.length() < 25 )
            {
                m_consultas(value.substring(0,6));

            }
            else if (value.length() > 24 && value.length() < 26)
            {
                m_consultas(value.substring(7, 6));
            }
            else if(value.length() < 27)
            {
                m_consultas(value.substring(8, value.length()-12));
            }
            else
            {
                m_consultas(value.substring(8, value.length()-13));
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void m_consultas(String cod_sku)
    {
        if (!cod_sku.isEmpty())
        {
            cod_sku_final = cod_sku;
            String descripcion = select.getDescripcionSku(cod_sku_final);
            if (descripcion != null && !descripcion.isEmpty())
            {
                edittext_descripcion_route.setText(descripcion);
                edittext_descripcion_route.setTextColor(Color.BLACK);
            }
            else
            {
                edittext_descripcion_route.setText("SKU no valido.");
                edittext_descripcion_route.setTextColor(Color.BLACK);
                Toast.makeText(this, "SKU no valido.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validationSave()
    {
        boolean respuesta = false;

        if(!edittext_route_sku.getText().toString().isEmpty() && !edittext_descripcion_route.getText().toString().isEmpty()
                && !edittext_descripcion_cantidad.getText().toString().isEmpty())
        {
            respuesta = true;
        }
        else
        {
            respuesta = false;
        }

        return respuesta;
    }

    private class GuardarRegistros extends AsyncTask<String,String,Integer>
    {

        ProgressDialog mSpinnerProgress;
        public GuardarRegistros() {

            mSpinnerProgress = new ProgressDialog(Route.this);
            mSpinnerProgress.setIndeterminate(true);
            mSpinnerProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerProgress.setMessage("Enviando Información");
            mSpinnerProgress.setCancelable(false);
            mSpinnerProgress.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPreExecute() {
            mSpinnerProgress.show();
        }

        @Override
        protected void onProgressUpdate(String...message) {
            super.onProgressUpdate(message);
            mSpinnerProgress.setMessage(message[0]);
        }

        @Override
        protected Integer doInBackground(String... params) {
            return guardarRegistros();
        }

        private int guardarRegistros() {
            int result = 0;

            try
            {
                List<Salidas> list = select.getSalidas();

                Gson json = new GsonBuilder().create();
                String datos = json.toJson(list);
                // Create http cliient object to send request to server
                HttpClient Client = new DefaultHttpClient();

                // Create URL string
                url_param = "set_salida?idEmpresa=" + pk.idEmpresa.replace(" ","")
                        + "&idAlmacen=" + pk.idAlmacen.replace(" ","")
                        + "&idRuta=" + id_Rutas
                        + "&idUsuario=" + pk.idUsuario
                        +"&detalle=";
                url_param_json = datos;

                String SetServerString = "";

                String encodedurl = URLEncoder.encode(url_param_json,"UTF-8");
                // Create Request to server and get response
                HttpGet httpget = new HttpGet(url_server+url_param+encodedurl);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = Client.execute(httpget, responseHandler);

                JSONObject jsonR = new JSONObject(SetServerString);

                if(jsonR.get("status").toString().equals("A"))
                {
                    result = 1;
                    msg = jsonR.get("description").toString();
                }
                else
                {
                    msg = jsonR.get("description").toString();
                    result = 0;
                }
            }
            catch (Exception ex)
            {
                System.out.println(" catch " + ex.toString());
            }
            finally
            {
            }
            return result;
        }

        protected void onPostExecute(Integer result){
            switch (result)
            {
                case 0:
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    delete.deleteSalidas();
                    spinner_ruta_route.setEnabled(true);
                    spinner_ruta_route.setSelection(0);
                    btn_enviar_route.setVisibility(View.GONE);
                    btn_consultar_route.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    private AlertDialog Alert(String desc_ruta){
        AlertDialog.Builder builder = new AlertDialog.Builder(Route.this);

        LayoutInflater inflater = Route.this.getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_consulta_route, null);

        TextView textView_titulo_alert = v.findViewById(R.id.textView_titulo_alert);

        textView_titulo_alert.setText("Elementos Capturados\n   Ruta: " + desc_ruta);

        ListView list_dynamic = v.findViewById(R.id.list_dynamic);
        listSalidas = select.getSalidas();
        registrosAdapter = new RegistrosAdapter(this, listSalidas);
        list_dynamic.setAdapter(registrosAdapter);

        builder.setView(v);

        return builder.create();
    }

    @Override
    public void onBackPressed()
    {
        delete.deleteSalidas();
        Intent intent = new Intent(Route.this, Menu_Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }
}
