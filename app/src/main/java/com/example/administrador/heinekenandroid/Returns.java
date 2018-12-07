package com.example.administrador.heinekenandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Delete;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.heineken.greendaoapp.db.Rutas;
import com.heineken.greendaoapp.db.Secciones;
import com.heineken.greendaoapp.db.UM;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.inicializarItem;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.obtenerPosicionItem;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Returns extends AppCompatActivity {

    Spinner spinner_ruta_returns, spinner_seccion_returns, spinner_unidad_medida_returns;
    EditText edittext_producto_sku_returns, edittext_descripcion_returns, edittext_cantidad_returns;
    Button btn_guardar;
    FloatingActionButton fab_returns;
    List<Rutas> listCatRutas; List<Secciones> listCatSecciones; List<UM> listCatUM;
    List<String> listIdRutas, listIdSecciones, listIdUm;
    Select select;
    PK pk;
    String url_param, id_Rutas, desc_Rutas, id_Secciones, desc_Secciones, id_Um, desc_Um, msg, cod_sku_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.returns_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        select = new Select(getApplicationContext());

        m_findViewById();
        m_carga_Adapter_Rutas();
        m_carga_Adapter_Secciones();
        m_setListener();
    }

    private void m_findViewById()
    {
        spinner_ruta_returns = findViewById(R.id.spinner_ruta_returns);
        spinner_seccion_returns = findViewById(R.id.spinner_seccion_returns);
        spinner_unidad_medida_returns = findViewById(R.id.spinner_unidad_medida_returns);

        edittext_producto_sku_returns = findViewById(R.id.edittext_producto_sku_returns);
        edittext_descripcion_returns = findViewById(R.id.edittext_descripcion_returns);
        edittext_cantidad_returns = findViewById(R.id.edittext_cantidad_returns);

        fab_returns = findViewById(R.id.fab_returns);

        btn_guardar = findViewById(R.id.btn_guardar);
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
        spinner_ruta_returns.setAdapter(adaptadorS);
    }

    private void m_carga_Adapter_Secciones()
    {
        listCatSecciones = select.getSeccionesDevoluciones();
        Iterator<Secciones> iteratorS = listCatSecciones.iterator();
        ArrayList<String> listS = new ArrayList<>();
        listIdSecciones = new ArrayList<>();
        while (iteratorS.hasNext()) {
            Secciones aux = iteratorS.next();
            listS.add(aux.getSeccion());
            listIdSecciones.add(aux.getId_seccion());
        }
        ArrayAdapter<String> adaptadorS = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listS);
        adaptadorS.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_seccion_returns.setAdapter(adaptadorS);
        spinner_seccion_returns.setSelection(2);
    }

    private void m_carga_Adapter_Um(String desc_Secciones)
    {
        if(desc_Secciones.equals("Lote"))
        {
            listCatUM = select.getUmTarimas();
        }
        else
        {
            listCatUM = select.getUmOthers();
        }
        Iterator<UM> iteratorU = listCatUM.iterator();
        ArrayList<String> listU = new ArrayList<>();
        listIdUm = new ArrayList<>();
        while (iteratorU.hasNext()) {
            UM aux = iteratorU.next();
            listU.add(aux.getUm());
            listIdUm.add(aux.getId_um());
        }
        //listU.add("-- --");
        //listIdUm.add("0");

        ArrayAdapter<String> adaptadorUM = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listU);
        adaptadorUM.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_unidad_medida_returns.setAdapter(adaptadorUM);
        //spinner_unidad_medida_returns.setSelection(obtenerPosicionItem(spinner_unidad_medida_returns, inicializarItem));
    }

    private void m_setListener()
    {
        fab_returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Returns.this).initiateScan();
            }
        });

        edittext_producto_sku_returns.addTextChangedListener(new TextWatcher() {
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

        edittext_producto_sku_returns.setOnKeyListener(new View.OnKeyListener() {
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

        spinner_ruta_returns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Rutas = String.valueOf(listIdRutas.get(position));
                desc_Rutas = spinner_ruta_returns.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_seccion_returns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Secciones = String.valueOf(listIdSecciones.get(position));
                desc_Secciones = spinner_seccion_returns.getSelectedItem().toString();
                m_carga_Adapter_Um(desc_Secciones);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_unidad_medida_returns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Um = String.valueOf(listIdUm.get(position));
                desc_Um = spinner_unidad_medida_returns.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validationSave() == true)
                {
                    GuardarRegistros task = new GuardarRegistros();
                    task.execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                edittext_producto_sku_returns.setText(result.getContents());
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
            String value = edittext_producto_sku_returns.getText().toString().trim();

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
                edittext_descripcion_returns.setText(descripcion);
                edittext_descripcion_returns.setTextColor(Color.BLACK);
            }
            else
            {
                edittext_descripcion_returns.setText("SKU no valido.");
                edittext_descripcion_returns.setTextColor(Color.BLACK);
                Toast.makeText(this, "SKU no valido.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validationSave()
    {
        boolean respuesta = false;

        if(!edittext_producto_sku_returns.getText().toString().isEmpty() && !edittext_descripcion_returns.getText().toString().isEmpty()
                && !edittext_cantidad_returns.getText().toString().isEmpty())
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

            mSpinnerProgress = new ProgressDialog(Returns.this);
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
                // Create http cliient object to send request to server
                HttpClient Client = new DefaultHttpClient();

                // Create URL string
                url_param = "set_devolucion?idEmpresa=" + pk.idEmpresa.replace(" ","")
                        + "&idAlmacen=" + pk.idAlmacen.replace(" ","")
                        + "&sku=" + cod_sku_final
                        + "&motivo=--"
                        + "&seccion=" + id_Secciones
                        + "&medida=" + id_Um
                        + "&cantidad=" + edittext_cantidad_returns.getText().toString().replace(" ", "")
                        + "&ruta=" + id_Rutas
                        + "&idUsuario=" + pk.idUsuario;

                String SetServerString = "";

                // Create Request to server and get response
                HttpGet httpget = new HttpGet(url_server+url_param);
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
                    result = 0;
                    msg = jsonR.get("description").toString();
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
                    cleanElements();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    private void cleanElements()
    {
        edittext_producto_sku_returns.setText("");
        edittext_descripcion_returns.setText("");
        edittext_cantidad_returns.setText("");

        spinner_ruta_returns.setSelection(0);
        spinner_seccion_returns.setSelection(2);
        spinner_unidad_medida_returns.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intent = new Intent(Returns.this, Menu_Principal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pk", pk);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Returns.this, Menu_Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }
}
