package com.example.administrador.heinekenandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatOrdinal;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatter;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Outputs extends AppCompatActivity {

    PK pk;
    FloatingActionButton fab;
    EditText edittext_outputs_tarimas, edittext_sku_outputs, edittext_descripcion_outputs, edittext_frescura_outputs;
    Button btn_guardar_outputs;
    String numero, msg;
    Select select;
    Date date;
    String url_param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outputs);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        select = new Select(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");

        m_findViewById();
        m_setListener();

        date = new Date();
        numero = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    }

    private void m_findViewById() {
        fab = findViewById(R.id.fab);
        edittext_outputs_tarimas = findViewById(R.id.edittext_outputs_tarimas);
        edittext_sku_outputs = findViewById(R.id.edittext_sku_outputs);
        edittext_descripcion_outputs = findViewById(R.id.edittext_descripcion_outputs);
        edittext_frescura_outputs = findViewById(R.id.edittext_frescura_outputs);
        btn_guardar_outputs = findViewById(R.id.btn_guardar_outputs);
    }

    private void m_setListener()
    {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(Outputs.this).initiateScan();
            }
        });

        edittext_outputs_tarimas.addTextChangedListener(new TextWatcher() {
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

        edittext_outputs_tarimas.setOnKeyListener(new View.OnKeyListener() {
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

        btn_guardar_outputs.setOnClickListener(new View.OnClickListener() {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                edittext_outputs_tarimas.setText(result.getContents());
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
            String value = edittext_outputs_tarimas.getText().toString().trim();

            if (value.length() <= 23)
            {
                Toast.makeText(this, "Error. Ingrese el Codigo de Barras", Toast.LENGTH_SHORT).show();
            }
            else if (value.length() > 23 && value.length() < 25 )
            {
                edittext_sku_outputs.setText(value.substring(0,6));
                edittext_sku_outputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(5, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(5, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 1)));
                    edittext_frescura_outputs.setText(fecha_final);
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {

                    edittext_frescura_outputs.setText(formatter.format(date));
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
            }
            else if (value.length() > 24 && value.length() < 26)
            {
                edittext_sku_outputs.setText(value.substring(7, 6));
                edittext_sku_outputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(6, 1).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(6, 1);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, 2)));
                    edittext_frescura_outputs.setText(fecha_final);
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
                catch(Exception ex)
                {
                    edittext_frescura_outputs.setText(formatter.format(date));
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
            }
            else if(value.length() < 27)
            {
                edittext_sku_outputs.setText(value.substring(8, value.length()-12));
                edittext_sku_outputs.setTextColor(Color.BLACK);
                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-18).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-18);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-19)));
                    edittext_frescura_outputs.setText(fecha_final);
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    edittext_frescura_outputs.setText(formatter.format(date));
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
            }
            else
            {
                edittext_sku_outputs.setText(value.substring(8, value.length()-13));
                edittext_sku_outputs.setTextColor(Color.BLACK);

                try
                {
                    String anio = "";
                    if(numero.equals("2020") && value.substring(7, value.length()-19).equals("9")){
                        anio = "2019";
                    }else{
                        anio = numero.substring(0, 3) + value.substring(7, value.length()-19);
                    }
                    String fecha_final = formatOrdinal(Integer.parseInt(anio), Integer.parseInt(value.substring(4, value.length()-20)));
                    edittext_frescura_outputs.setText(fecha_final);
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
                catch (Exception ex)
                {
                    edittext_frescura_outputs.setText(formatter.format(date));
                    edittext_frescura_outputs.setTextColor(Color.BLACK);
                }
            }

            m_consultas();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void m_consultas()
    {
        if (!edittext_sku_outputs.getText().toString().isEmpty())
        {
            String descripcion = select.getDescripcionSku(edittext_sku_outputs.getText().toString());
            if (descripcion != null && !descripcion.isEmpty())
            {
                edittext_descripcion_outputs.setText(descripcion);
                edittext_descripcion_outputs.setTextColor(Color.BLACK);
            }
            else
            {
                edittext_descripcion_outputs.setText("SKU no valido.");
                edittext_descripcion_outputs.setTextColor(Color.BLACK);
                Toast.makeText(this, "SKU no valido.", Toast.LENGTH_SHORT).show();
            }
        }

        if (edittext_frescura_outputs.getText().toString().isEmpty())
        {
            edittext_frescura_outputs.setText(formatter.format(date));
            edittext_frescura_outputs.setTextColor(Color.BLACK);
        }
    }

    private boolean validationSave()
    {
        boolean respuesta = false;

        if(!edittext_outputs_tarimas.getText().toString().isEmpty() && !edittext_sku_outputs.getText().toString().isEmpty()
                && !edittext_descripcion_outputs.getText().toString().isEmpty() && !edittext_frescura_outputs.getText().toString().isEmpty())
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

            mSpinnerProgress = new ProgressDialog(Outputs.this);
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
                url_param = "set_picking?idEmpresa=" + pk.idEmpresa.replace(" ","")
                        + "&idAlmacen=" + pk.idAlmacen.replace(" ","")
                        + "&fechaFrescura=" + edittext_frescura_outputs.getText().toString().trim().replace(" ","")
                        + "&sku=" + edittext_sku_outputs.getText().toString().trim().replace(" ","")
                        + "&idSap=" + edittext_outputs_tarimas.getText().toString().trim().replace(" ", "")
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
                else if(jsonR.get("status").toString().equals("C"))
                {
                    result = 2;
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
                case 2:
                    cleanElements();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    private void cleanElements()
    {
        edittext_outputs_tarimas.setText("");
        edittext_sku_outputs.setText("");
        edittext_descripcion_outputs.setText("");
        edittext_frescura_outputs.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Outputs.this, Menu_Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }
}
