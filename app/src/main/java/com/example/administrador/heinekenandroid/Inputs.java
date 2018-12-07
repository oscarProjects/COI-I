package com.example.administrador.heinekenandroid;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatOrdinal;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatter;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Inputs extends AppCompatActivity {

    FloatingActionButton fab;
    EditText edittext_inputs_pedidos, edittext_tarimas_inputs, edittext_sku_inputs, edittext_descripcion_inputs, edittext_frescura_inputs;
    Spinner sp_buffer_inputs;
    Button btn_guardar_inputs;
    List lstBuffer, lstId;
    String id_buffer, desc_buffer;
    String numero, msg;
    Select select;
    Date date;
    String url_param;
    PK pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        select = new Select(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        lstBuffer = new ArrayList<String>();
        lstId = new ArrayList<String>();

        m_findViewById();
        m_setListener();

        DescargaBuffer task1 = new DescargaBuffer();
        task1.execute();

        date = new Date();
        numero = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    }

    private void m_findViewById() {
        fab = findViewById(R.id.fab);
        edittext_inputs_pedidos = findViewById(R.id.edittext_inputs_pedidos);
        edittext_tarimas_inputs = findViewById(R.id.edittext_tarimas_inputs);
        edittext_sku_inputs = findViewById(R.id.edittext_sku_inputs);
        edittext_descripcion_inputs = findViewById(R.id.edittext_descripcion_inputs);
        edittext_frescura_inputs = findViewById(R.id.edittext_frescura_inputs);
        sp_buffer_inputs = findViewById(R.id.sp_buffer_inputs);
        sp_buffer_inputs.setEnabled(false);
        btn_guardar_inputs = findViewById(R.id.btn_guardar_inputs);
    }

    private void m_setListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Inputs.this).initiateScan();
            }
        });

        edittext_tarimas_inputs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edittext_tarimas_inputs.setOnKeyListener(new View.OnKeyListener() {
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

        sp_buffer_inputs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_buffer = String.valueOf(lstId.get(position));
                desc_buffer = sp_buffer_inputs.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_guardar_inputs.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                edittext_tarimas_inputs.setText(result.getContents());
                m_validation();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void m_validation(){
        try
        {
            String value = edittext_tarimas_inputs.getText().toString().trim();

            if (value.length() <= 23)
            {
                Toast.makeText(this, "Error. Ingrese el Codigo de Barras", Toast.LENGTH_SHORT).show();
            }
            else if (value.length() > 23 && value.length() < 25 )
            {
                edittext_sku_inputs.setText(value.substring(0,6));
                edittext_sku_inputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(5, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(5, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 1)));
                    edittext_frescura_inputs.setText(fecha_final);
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {

                    edittext_frescura_inputs.setText(formatter.format(date));
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
            }
            else if (value.length() > 24 && value.length() < 26)
            {
                edittext_sku_inputs.setText(value.substring(7, 6));
                edittext_sku_inputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(6, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(6, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 2)));
                    edittext_frescura_inputs.setText(fecha_final);
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {
                    edittext_frescura_inputs.setText(formatter.format(date));
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
            }
            else if(value.length() < 27)
            {
                edittext_sku_inputs.setText(value.substring(8, value.length()-12));
                edittext_sku_inputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-18).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-18);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-19)));
                    edittext_frescura_inputs.setText(fecha_final);
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    edittext_frescura_inputs.setText(formatter.format(date));
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
            }
            else
            {
                edittext_sku_inputs.setText(value.substring(8, value.length()-13));
                edittext_sku_inputs.setTextColor(Color.BLACK);

                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-19).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-19);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-20)));
                    edittext_frescura_inputs.setText(fecha_final);
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    edittext_frescura_inputs.setText(formatter.format(date));
                    edittext_frescura_inputs.setTextColor(Color.BLACK);
                }
            }

            m_consultas();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void m_consultas(){
        if (!edittext_sku_inputs.getText().toString().isEmpty())
        {
            String descripcion = select.getDescripcionSku(edittext_sku_inputs.getText().toString());
            if (descripcion != null && !descripcion.isEmpty())
            {
                edittext_descripcion_inputs.setText(descripcion);
                edittext_descripcion_inputs.setTextColor(Color.BLACK);
            }
            else
            {
                edittext_descripcion_inputs.setText("SKU no valido.");
                edittext_descripcion_inputs.setTextColor(Color.BLACK);
                Toast.makeText(this, "SKU no valido.", Toast.LENGTH_SHORT).show();
            }
        }

        if (edittext_frescura_inputs.getText().toString().isEmpty())
        {
            edittext_frescura_inputs.setText(formatter.format(date));
            edittext_frescura_inputs.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Inputs.this, Menu_Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }

    private class DescargaBuffer extends AsyncTask<String,String,Integer> {

        ProgressDialog mSpinnerProgress;

        public DescargaBuffer() {
            mSpinnerProgress = new ProgressDialog(Inputs.this);
            mSpinnerProgress.setIndeterminate(true);
            mSpinnerProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerProgress.setMessage("Descargando Buffer");
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
            return descargaBuffer();
        }

        private int descargaBuffer() {
            int result = 0;

                try
                {
                    // Create http cliient object to send request to server
                    HttpClient Client = new DefaultHttpClient();
                    // Create URL string
                    String url_param = "get_buffer?id_empresa=" + pk.idEmpresa + "&id_almacen=" + pk.idAlmacen;
                    String SetServerString = "";

                    // Create Request to server and get response
                    HttpGet httpget = new HttpGet(url_server+url_param);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    SetServerString = Client.execute(httpget, responseHandler);

                    JSONArray jsonR = new JSONArray(SetServerString);
                    for(int x = 0; x < jsonR.length(); x++){
                        JSONObject subObj = jsonR.getJSONObject(x);
                        lstId.add(subObj.get("id").toString());
                        lstBuffer.add(subObj.get("buffer").toString());
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

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Inputs.this, android.R.layout.simple_spinner_item, lstBuffer);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_buffer_inputs.setAdapter(dataAdapter);

            Toast.makeText(getApplicationContext(), "Descarga Exitosa", Toast.LENGTH_SHORT).show();
            mSpinnerProgress.dismiss();
        }

    }

    private class GuardarRegistros extends AsyncTask<String,String,Integer> {

        ProgressDialog mSpinnerProgress;
        public GuardarRegistros() {

            mSpinnerProgress = new ProgressDialog(Inputs.this);
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
                    // Create
                    // http cliient object to send request to server
                    HttpClient Client = new DefaultHttpClient();

                    // Create URL string
                    url_param = "set_entrada?idEmpresa=" + pk.idEmpresa.replace(" ","")
                            + "&idAlmacen=" + pk.idAlmacen.replace(" ","")
                            + "&sku=" + edittext_sku_inputs.getText().toString().trim().replace(" ","")
                            + "&pedido=" + edittext_inputs_pedidos.getText().toString().trim().replace(" ","")
                            + "&f_frescura=" + edittext_frescura_inputs.getText().toString().trim().replace(" ","")
                            + "&codigo=" + edittext_tarimas_inputs.getText().toString().trim().replace(" ", "");
                            if(!id_buffer.equals("----"))
                            {
                                url_param += "&buffer=" + id_buffer.trim().replace(" ","");
                            }
                    url_param += "&idUsuario=" + pk.idUsuario;

                    String SetServerString = "";

                    // Create Request to server and get response
                    HttpGet httpget = new HttpGet(url_server+url_param);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    SetServerString = Client.execute(httpget, responseHandler);

                    JSONObject jsonR = new JSONObject(SetServerString);

                    if(jsonR.get("status").toString().equals("A"))
                    {
                        if (jsonR.get("description").toString().equals("-"))
                        {
                            result = 1;
                            sp_buffer_inputs.setEnabled(true);
                        }
                        else
                        {
                            msg = jsonR.get("description").toString();
                            result = 2;
                        }
                    }
                    else if(jsonR.get("status").toString().equals("C"))
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
                    Toast.makeText(getApplicationContext(), "Seleccione un Buffer", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    cleanElements();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    private boolean validationSave()
    {
        boolean respuesta = false;

        if(!edittext_inputs_pedidos.getText().toString().isEmpty() && !edittext_tarimas_inputs.getText().toString().isEmpty()
        && !edittext_sku_inputs.getText().toString().isEmpty() && !edittext_descripcion_inputs.getText().toString().isEmpty()
        && !edittext_frescura_inputs.getText().toString().isEmpty())
        {
            respuesta = true;
        }
        else
            {
                respuesta = false;
            }

        return respuesta;
    }

    private void cleanElements()
    {
        //edittext_inputs_pedidos.setText("");
        edittext_tarimas_inputs.setText("");
        edittext_sku_inputs.setText("");
        edittext_descripcion_inputs.setText("");
        edittext_frescura_inputs.setText("");
        sp_buffer_inputs.setSelection(0);
        sp_buffer_inputs.setEnabled(false);
    }
}
