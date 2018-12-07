package com.example.administrador.heinekenandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.RutaDetalleAdapter;
import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.Extras.Ruta_Detalle;
import com.example.administrador.heinekenandroid.Extras.Variables_Generales;
import com.example.administrador.heinekenandroid.dbQuerys.Delete;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;
import com.example.administrador.heinekenandroid.dbQuerys.Select;
import com.heineken.greendaoapp.db.Familia;
import com.heineken.greendaoapp.db.Lotes;
import com.heineken.greendaoapp.db.Productos;
import com.heineken.greendaoapp.db.Rutas;
import com.heineken.greendaoapp.db.Secciones;
import com.heineken.greendaoapp.db.UM;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.*;

public class Route_Detail extends AppCompatActivity {

    public TextView textView3;
    List<Rutas> listCatRutas;
    List<String> listIdRutas;
    Select select; Insert insert; Delete delete;
    String url_param, id_Rutas_Detalle, desc_Rutas_Detalle;
    PK pk;
    Date date;
    Ruta_Detalle ruta_detalle;
    ArrayList arrayListRutaDetalle;
    ListView listView_dynamic_ruta_detalle;
    RutaDetalleAdapter adapter;
    Button btn_guardar_route_detail;
    String ubicacion, msg;
    int contadorCapurados[];

    private ListView lv;
    private EditText et;
    int textlength = 0;
    private ArrayList<String> array_sort = new ArrayList<String>();
    ArrayList<String> listR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.route_detail_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        select = new Select(getApplicationContext());
        insert = new Insert(getApplicationContext());
        delete = new Delete(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        pk = (PK) extras.get("pk");
        ubicacion = (String) extras.get("ubicacion");

        date = new Date();

        m_findViewById();
        m_carga_Adapter_Rutas();
        m_setListener();
    }

    private void m_findViewById() {
        textView3 = findViewById(R.id.textView3);

        listView_dynamic_ruta_detalle = findViewById(R.id.listView_dynamic_ruta_detalle);

        btn_guardar_route_detail = findViewById(R.id.btn_guardar_route_detail);
        btn_guardar_route_detail.setVisibility(View.GONE);

        lv = findViewById(R.id.ListView01);
        et = findViewById(R.id.EditText01);
    }

    private void m_carga_Adapter_Rutas()
    {
        listCatRutas = select.getRutas();
        Iterator<Rutas> iteratorS = listCatRutas.iterator();
        listR = new ArrayList<>();
        listIdRutas = new ArrayList<>();
        while (iteratorS.hasNext()) {
            Rutas aux = iteratorS.next();
            listR.add(aux.getRuta());
            listIdRutas.add(aux.getId_ruta());
        }

        ArrayAdapter<String> adaptadorS = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, listR);
        adaptadorS.setDropDownViewResource(android.R.layout.simple_list_item_1);
        lv.setAdapter(adaptadorS);
    }

    private void m_setListener()
    {
        et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Abstract Method of TextWatcher Interface.
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textlength = et.getText().length();
                array_sort.clear();

                for (int i = 0; i < listR.size(); i++) {
                    if (textlength <= listR.get(i).length()) {
                        if (et.getText().toString().equalsIgnoreCase((String) listR.get(i).subSequence(0, textlength))) {
                            array_sort.add(listR.get(i));
                        }
                    }
                }

                lv.setAdapter(new ArrayAdapter<String>(Route_Detail.this, android.R.layout.simple_list_item_1, array_sort));
            }
        });

        et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    return true;
                }
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id_Rutas_Detalle = String.valueOf(listIdRutas.get(position));
                desc_Rutas_Detalle =(String) (lv.getItemAtPosition(position));

                et.setText(desc_Rutas_Detalle);
                if(!desc_Rutas_Detalle.equals("-- --"))
                {
                    View viewteclado = Route_Detail.this.getCurrentFocus();
                    if (viewteclado != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(viewteclado.getWindowToken(), 0);
                    }
                    DescargaDetallesRuta task = new DescargaDetallesRuta();
                    task.execute();
                }
                else
                {
                    listView_dynamic_ruta_detalle.setVisibility(View.GONE);
                }
            }
        });

        listView_dynamic_ruta_detalle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = view.findViewById(R.id.textView_cantidad);
                TextView textView2 = view.findViewById(R.id.textView_cantidad_detalle);

                String cantidad = textView.getText().toString().trim().replace(" ", "");
                String cantidadDetalle = textView2.getText().toString().trim().replace(" ", "");

                if(cantidad.substring(9, cantidad.length()).equals(cantidadDetalle.substring(11, cantidadDetalle.length())))
                {
                    Toast.makeText(Route_Detail.this, "Este producto ya fue Capturado.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Object listItem = listView_dynamic_ruta_detalle.getItemAtPosition(position);
                    Intent intent = new Intent(view.getContext(), Read_Route_Details.class);
                    intent.putExtra("ruta_detalle", (Serializable) listItem);
                    intent.putExtra("pk", pk);
                    intent.putExtra("id_Rutas_Detalle", id_Rutas_Detalle);
                    intent.putExtra("desc_Rutas_Detalle", desc_Rutas_Detalle);
                    startActivity(intent);
                }
            }
        });

        btn_guardar_route_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarRuta task1 = new ValidarRuta();
                task1.execute();
            }
        });

    }

    private class DescargaDetallesRuta extends AsyncTask<String,String,Integer> {


        ProgressDialog mSpinnerProgress;
        public DescargaDetallesRuta() {

            mSpinnerProgress = new ProgressDialog(Route_Detail.this);
            mSpinnerProgress.setIndeterminate(true);
            mSpinnerProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerProgress.setMessage("Descargando Detalles");
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
            return descargaCatalogos();
        }

        private int descargaCatalogos() {
            int result = 0;

            try
            {
                // Create http cliient object to send request to server
                HttpClient Client = new DefaultHttpClient();

                // Create URL string
                url_param = "get_ruta_detalle?id_empresa=" + pk.idEmpresa + "&id_almacen=" + pk.idAlmacen
                        + "&id_ruta=" + id_Rutas_Detalle
                        + "&fecha=" + formatter.format(date);

                String SetServerString = "";

                // Create Request to server and get response
                HttpGet httpget = new HttpGet(url_server+url_param);

                System.out.println("URL " + (url_server+url_param));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = Client.execute(httpget, responseHandler);

                JSONArray respJSON = new JSONArray(SetServerString);

                arrayListRutaDetalle = new ArrayList(); //instanciamos el arrayList que va a ir el el AdapterCategory

                if(respJSON.length() == 0)
                {
                    result = 0;
                }
                else
                {
                    result = 1;
                    contadorCapurados = new int[respJSON.length()];
                    for(int x=0; x<respJSON.length(); x++)
                    {
                        JSONObject obj = respJSON.getJSONObject(x);

                        String id = obj.get("id").toString();
                        String sku = obj.get("sku").toString();
                        String idum = obj.get("idum").toString();
                        String cantidad = obj.get("cantidad").toString();
                        String cantidadDetalle = obj.get("cantidadDetalle").toString();
                        String familia = obj.get("familia").toString();
                        String idFamilia = obj.get("idFamilia").toString();
                        contadorCapurados[x] = Integer.parseInt(obj.get("completo").toString().trim().replace(" ", ""));

                        ruta_detalle = new Ruta_Detalle(id, sku, idum, cantidad, cantidadDetalle, familia, idFamilia); //colocamos los valores en el constructor de category
                        arrayListRutaDetalle.add(ruta_detalle);
                    }
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
                    listView_dynamic_ruta_detalle.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No hay elementos disponibles.", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    m_llenar(contadorCapurados);
                    break;
            }

            mSpinnerProgress.dismiss();
        }

    }

    private void m_llenar(int[] contadorCapurados) {
        adapter = new RutaDetalleAdapter(Route_Detail.this, arrayListRutaDetalle);
        listView_dynamic_ruta_detalle.setAdapter(adapter);
        listView_dynamic_ruta_detalle.setVisibility(View.VISIBLE);
        m_encender(contadorCapurados);
    }

    private void m_encender(int[] contadorCapurados) {
        int total = 1;

        for(int r = 0; r < contadorCapurados.length; r++)
        {
            total*=contadorCapurados[r];
        }

        if(total == 1)
        {
            btn_guardar_route_detail.setVisibility(View.VISIBLE);
        }
        else
        {
            btn_guardar_route_detail.setVisibility(View.GONE);
        }
    }

    private class ValidarRuta extends AsyncTask<String,String,Integer>
    {
        ProgressDialog mSpinnerProgress;

        public ValidarRuta() {
            mSpinnerProgress = new ProgressDialog(Route_Detail.this);
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

                //get_ruta_actualiza_detalle?id_empresa=1&id_almacen=1&id_ruta=3&id_familia=5&sku=139011&cantidad=10&fecha=2018-11-01

                // Create URL string
                url_param = "get_ruta_actualiza?"
                        + "id_empresa=" + pk.idEmpresa.replace(" ","")
                        + "&id_almacen=" + pk.idAlmacen.replace(" ","")
                        + "&id_ruta=" + id_Rutas_Detalle.trim().replace(" ","")
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

                    break;
            }
            mSpinnerProgress.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent intent = new Intent(Route_Detail.this, Menu_Principal.class);
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
        Intent intent = new Intent(Route_Detail.this, Menu_Principal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("pk", pk);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        DescargaDetallesRuta task = new DescargaDetallesRuta();
        task.execute();
        super.onResume();
    }

}
