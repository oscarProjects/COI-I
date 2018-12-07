package com.example.administrador.heinekenandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.Extras.Ruta_Detalle;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.Date;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.formatter;
import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Read_Route_Details extends AppCompatActivity {

    Ruta_Detalle ruta_detalle; PK pk;
    TextView textView_set_sku_read, textView_set_desc_read, textView_set_um_read, textView_set_total_read;
    EditText edittext_producto_sku_rrd, edittext_num;
    FloatingActionButton fab_rrd;
    int cod_sku_barcode, cod_sku_descarga, cantidad;
    Button btn_guardar_rrd;
    String url_param, msg, id_Rutas_Detalle, desc_Rutas_Detalle, cod_sku_base;
    Date date;
    Select select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read__route__details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Bundle extras = getIntent().getExtras();
        ruta_detalle = (Ruta_Detalle) extras.get("ruta_detalle");
        pk = (PK) extras.get("pk");
        id_Rutas_Detalle = (String) extras.get("id_Rutas_Detalle");
        desc_Rutas_Detalle = (String) extras.get("desc_Rutas_Detalle");

        setTitle("Familia : " + ruta_detalle.getFamilia() + "           Ruta : " + desc_Rutas_Detalle);

        select = new Select(getApplicationContext());

        m_findViewById();
        m_setText();
        m_setListener();
    }

    private void m_findViewById() {
        textView_set_sku_read = findViewById(R.id.textView_set_sku_read);
        textView_set_desc_read = findViewById(R.id.textView_set_desc_read);
        textView_set_um_read = findViewById(R.id.textView_set_um_read);
        textView_set_total_read = findViewById(R.id.textView_set_total_read);

        edittext_producto_sku_rrd = findViewById(R.id.edittext_producto_sku_rrd);
        edittext_num = findViewById(R.id.edittext_num);
        edittext_num.setEnabled(false);

        fab_rrd = findViewById(R.id.fab_rrd);

        btn_guardar_rrd = findViewById(R.id.btn_guardar_rrd);
        btn_guardar_rrd.setVisibility(View.GONE);

    }

    private void m_setText() {
        textView_set_sku_read.setText(ruta_detalle.getId());
        textView_set_desc_read.setText(ruta_detalle.getSku());
        textView_set_um_read.setText(ruta_detalle.getIdum());
        textView_set_total_read.setText(ruta_detalle.getCantidad());

        edittext_producto_sku_rrd = findViewById(R.id.edittext_producto_sku_rrd);
        edittext_producto_sku_rrd.setFocusable(true);

        cod_sku_descarga = Integer.parseInt(textView_set_sku_read.getText().toString().trim().replace(" ", ""));
        cantidad = Integer.parseInt(textView_set_total_read.getText().toString());
    }

    private void m_setListener() {

        fab_rrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(Read_Route_Details.this).initiateScan();
            }
        });

        edittext_producto_sku_rrd.setOnKeyListener(new View.OnKeyListener() {
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

        edittext_num.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if(!edittext_num.getText().toString().isEmpty()){
                        int total = Integer.parseInt(edittext_num.getText().toString());
                        if(cantidad == total){
                            edittext_num.setEnabled(false);
                            btn_guardar_rrd.setVisibility(View.VISIBLE);
                        }else{
                            edittext_num.setEnabled(true);
                            btn_guardar_rrd.setVisibility(View.GONE);
                            Toast.makeText(Read_Route_Details.this, "La cantidad debe ser igual al Total.", Toast.LENGTH_LONG).show();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        btn_guardar_rrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarRegistros task = new GuardarRegistros();
                task.execute();
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
                edittext_producto_sku_rrd.setText(result.getContents());
                m_validation_ActivityResult();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void m_validation()
    {
        try
        {
            String value = edittext_producto_sku_rrd.getText().toString().trim();

            if (value.length() < 6)
            {
                Toast.makeText(this, "Codigo no valido.", Toast.LENGTH_LONG).show();
            }
            else if (value.length() < 7)
            {
                m_consultas(value);
            }
            else
                {
                    Toast.makeText(this, "Codigo no valido.", Toast.LENGTH_LONG).show();
                }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void m_validation_ActivityResult()
    {
        try
        {
            String value = edittext_producto_sku_rrd.getText().toString().trim().replace(" ", "");
            m_consultas_ActivityResult(value);
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
            cod_sku_barcode = Integer.parseInt(cod_sku);

            System.out.println(" cod_sku_barcode " + cod_sku_barcode + " cod_sku_descarga " + cod_sku_descarga);

            if(cod_sku_barcode == cod_sku_descarga)
            {
                edittext_num.setEnabled(true);
                edittext_num.setFocusable(true);// to set focus on EditText
                edittext_producto_sku_rrd.setFocusable(false);
                Toast.makeText(this, "Ingrese la cantidad.", Toast.LENGTH_LONG).show();
            }
            else
                {
                    edittext_num.setEnabled(false);
                    Toast.makeText(this, "Codigo no valido.", Toast.LENGTH_LONG).show();
                }

        }
    }

    private void m_consultas_ActivityResult(String cod_sku)
    {
        if (!cod_sku.isEmpty())
        {
            System.out.println(" cod_sku_descarga " + cod_sku_descarga);
            cod_sku_base = select.getCodBarSku(String.valueOf(cod_sku_descarga));
            System.out.println(" cod_sku_base " + cod_sku_base);

            if(cod_sku.equals(cod_sku_base))
            {
                edittext_num.setEnabled(true);
                edittext_num.setFocusable(true);// to set focus on EditText
                edittext_producto_sku_rrd.setFocusable(false);
                Toast.makeText(this, "Ingrese la cantidad.", Toast.LENGTH_LONG).show();
            }
            else
            {
                edittext_num.setEnabled(false);
                Toast.makeText(this, "Codigo no valido.", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class GuardarRegistros extends AsyncTask<String,String,Integer>
    {
        ProgressDialog mSpinnerProgress;

        public GuardarRegistros() {
            mSpinnerProgress = new ProgressDialog(Read_Route_Details.this);
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
                date = new Date();
                // Create http cliient object to send request to server
                HttpClient Client = new DefaultHttpClient();

                // Create URL string
                url_param = "get_ruta_actualiza_detalle?"
                        + "id_empresa=" + pk.idEmpresa.replace(" ","")
                        + "&id_almacen=" + pk.idAlmacen.replace(" ","")
                        + "&id_ruta=" + id_Rutas_Detalle.trim().replace(" ","")
                        + "&id_familia=" + ruta_detalle.getId_familia().trim().replace(" ","")
                        + "&sku=" + ruta_detalle.getId().trim().replace(" ", "")
                        + "&cantidad=" + ruta_detalle.getCantidad().trim().replace(" ", "")
                        + "&fecha=" + formatter.format(date).trim().replace(" ", "")
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
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        alertView();
    }

    private void alertView() {
        final AlertDialog alertDialog = new AlertDialog.Builder(Read_Route_Details.this).create();
        alertDialog.setInverseBackgroundForced(true);
        alertDialog.setTitle("IMPORTANTE:");
        alertDialog.setMessage("Esta seguro que desea salir?\n Se perdera el conteo del producto.");
        alertDialog.setIcon(R.drawable.logo_new);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
