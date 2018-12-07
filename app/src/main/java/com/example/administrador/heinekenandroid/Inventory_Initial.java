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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.heineken.greendaoapp.db.Lotes;
import com.heineken.greendaoapp.db.Rutas;
import com.heineken.greendaoapp.db.Secciones;
import com.heineken.greendaoapp.db.UM;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatOrdinal;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatter;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Inventory_Initial extends AppCompatActivity {

    PK pk;
    List<Lotes> listCatLotes; List<Secciones> listCatSecciones; List<UM> listCatUM;
    List<String> listIdLotes, listIdSecciones, listIdUm;
    Spinner spinner_lote_inventory_initial, spinner_ubicacion_inventory_initial, spinner_unidad_medida_inventory_initial;
    EditText edittext_sku_cod_i_i, edittext_cantidad_i_i;
    TextView textView_set_descripcion_i_i, textView_set_frescura_i_i;
    Button btn_guardar;
    FloatingActionButton fab_i_i;
    Select select;
    String url_param, id_Lotes, desc_Lotes, id_Secciones, desc_Secciones, id_Um, desc_Um, msg, cod_sku_final, numero;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory__initial);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.inventario_inicial_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        select = new Select(getApplicationContext());
        numero = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));


        date = new Date();

        m_findViewById();
        m_carga_Adapter_Lotes();
        m_carga_Adapter_Secciones();
        m_setListener();
    }

    private void m_findViewById()
    {
        spinner_lote_inventory_initial = findViewById(R.id.spinner_lote_inventory_initial);
        spinner_ubicacion_inventory_initial = findViewById(R.id.spinner_ubicacion_inventory_initial);
        spinner_unidad_medida_inventory_initial = findViewById(R.id.spinner_unidad_medida_inventory_initial);

        edittext_sku_cod_i_i = findViewById(R.id.edittext_sku_cod_i_i);
        edittext_cantidad_i_i = findViewById(R.id.edittext_cantidad_i_i);

        textView_set_descripcion_i_i = findViewById(R.id.textView_set_descripcion_i_i);
        textView_set_frescura_i_i = findViewById(R.id.textView_set_frescura_i_i);

        fab_i_i = findViewById(R.id.fab_i_i);

        btn_guardar = findViewById(R.id.btn_guardar);
    }

    private void m_carga_Adapter_Lotes()
    {
        listCatLotes = select.getLotes();
        Iterator<Lotes> iteratorS = listCatLotes.iterator();
        ArrayList<String> listR = new ArrayList<>();
        listIdLotes = new ArrayList<>();
        while (iteratorS.hasNext()) {
            Lotes aux = iteratorS.next();
            listR.add(aux.getLote());
            listIdLotes.add(aux.get_id_());
        }
        ArrayAdapter<String> adaptadorS = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listR);
        adaptadorS.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_lote_inventory_initial.setAdapter(adaptadorS);
    }

    private void m_carga_Adapter_Secciones()
    {
        listCatSecciones = select.getSecciones();
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
        spinner_ubicacion_inventory_initial.setAdapter(adaptadorS);
        spinner_ubicacion_inventory_initial.setSelection(2);
    }

    private void m_carga_Adapter_Um(String desc_Secciones)
    {
        if(desc_Secciones.equals("Lote") || desc_Secciones.equals("Buffer"))
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
        spinner_unidad_medida_inventory_initial.setAdapter(adaptadorUM);
        //spinner_unidad_medida_returns.setSelection(obtenerPosicionItem(spinner_unidad_medida_returns, inicializarItem));
    }

    private void m_setListener()
    {
        fab_i_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Inventory_Initial.this).initiateScan();
            }
        });

        edittext_sku_cod_i_i.addTextChangedListener(new TextWatcher() {
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

        edittext_sku_cod_i_i.setOnKeyListener(new View.OnKeyListener() {
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

        spinner_lote_inventory_initial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Lotes = String.valueOf(listIdLotes.get(position));
                desc_Lotes = spinner_lote_inventory_initial.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_ubicacion_inventory_initial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Secciones = String.valueOf(listIdSecciones.get(position));
                desc_Secciones = spinner_ubicacion_inventory_initial.getSelectedItem().toString();
                m_carga_Adapter_Um(desc_Secciones);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_unidad_medida_inventory_initial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_Um = String.valueOf(listIdUm.get(position));
                desc_Um = spinner_unidad_medida_inventory_initial.getSelectedItem().toString();

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
                edittext_sku_cod_i_i.setText(result.getContents());
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
            String value = edittext_sku_cod_i_i.getText().toString().trim();

            if (value.length() <= 23)
            {
                Toast.makeText(this, "Error. Ingrese el Codigo de Barras", Toast.LENGTH_SHORT).show();
            }
            else if (value.length() > 23 && value.length() < 25 )
            {
                //AQUI FRESCURA
                m_consultas(value.substring(0,6));
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(5, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(5, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 1)));
                    textView_set_frescura_i_i.setText(fecha_final);
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {

                    textView_set_frescura_i_i.setText(formatter.format(date));
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }

            }
            else if (value.length() > 24 && value.length() < 26)
            {
                //AQUI FRESCURA
                m_consultas(value.substring(7, 6));
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(6, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(6, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 2)));
                    textView_set_frescura_i_i.setText(fecha_final);
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {
                    textView_set_frescura_i_i.setText(formatter.format(date));
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
            }
            else if(value.length() < 27)
            {
                //AQUI FRESCURA
                m_consultas(value.substring(8, value.length()-12));
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-18).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-18);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-19)));
                    textView_set_frescura_i_i.setText(fecha_final);
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    textView_set_frescura_i_i.setText(formatter.format(date));
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
            }
            else
            {
                //AQUI FRESCURA
                m_consultas(value.substring(8, value.length()-13));
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-19).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-19);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-20)));
                    textView_set_frescura_i_i.setText(fecha_final);
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    textView_set_frescura_i_i.setText(formatter.format(date));
                    textView_set_frescura_i_i.setTextColor(Color.BLACK);
                }
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
                textView_set_descripcion_i_i.setText(descripcion);
                textView_set_descripcion_i_i.setTextColor(Color.BLACK);
            }
            else
            {
                textView_set_descripcion_i_i.setText("SKU no valido.");
                textView_set_descripcion_i_i.setTextColor(Color.BLACK);
                Toast.makeText(this, "SKU no valido.", Toast.LENGTH_SHORT).show();
                cleanElements();
            }
        }

        if(textView_set_frescura_i_i.getText().toString().isEmpty())
        {
            textView_set_frescura_i_i.setText(formatter.format(date));
            textView_set_frescura_i_i.setTextColor(Color.BLACK);
        }
    }

    private boolean validationSave()
    {
        boolean respuesta = false;

        if(!edittext_sku_cod_i_i.getText().toString().isEmpty() && !edittext_cantidad_i_i.getText().toString().isEmpty()
                && !textView_set_descripcion_i_i.getText().toString().isEmpty() && !textView_set_frescura_i_i.getText().toString().isEmpty())
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

            mSpinnerProgress = new ProgressDialog(Inventory_Initial.this);
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
                url_param = "set_inventario?idEmpresa=" + pk.idEmpresa.replace(" ","")
                        + "&idAlmacen=" + pk.idAlmacen.replace(" ","")
                        + "&idUbicacion=" + id_Secciones
                        + "&idUm=" + id_Um
                        + "&sku=" + cod_sku_final
                        + "&cantidad=" + edittext_cantidad_i_i.getText().toString().replace(" ", "")
                        + "&lote=" + id_Lotes
                        + "&frescura=" + textView_set_frescura_i_i.getText().toString()
                        + "&idSap=" + edittext_sku_cod_i_i.getText().toString().replace(" ", "")
                        + "&idUsuario=" + pk.idUsuario;

                String SetServerString = "";

                System.out.println(" URL " + (url_server+url_param));

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
        edittext_sku_cod_i_i.setText("");
        textView_set_descripcion_i_i.setText("");
        edittext_cantidad_i_i.setText("");
        textView_set_frescura_i_i.setText("");
        spinner_lote_inventory_initial.setSelection(0);
        spinner_ubicacion_inventory_initial.setSelection(2);
        spinner_unidad_medida_inventory_initial.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(Inventory_Initial.this, Configuracion.class);
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
        Intent intent = new Intent(Inventory_Initial.this, Configuracion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }
}
