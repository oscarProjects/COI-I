package com.example.administrador.heinekenandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.Descarga_Informacion;
import com.example.administrador.heinekenandroid.Extras.PK;
import com.example.administrador.heinekenandroid.dbQuerys.Delete;
import com.example.administrador.heinekenandroid.dbQuerys.Select;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.*;

public class MainActivity extends AppCompatActivity {

    public static final int MULTIPLE_PERMISSION_REQUEST = 3;
    private static String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
    };

    EditText txtUsuario, txtPassw;
    FloatingActionButton fab_connections;
    String msg, jsonparam, myIMEI;

    JSONObject jparam;
    Delete delete;
    Select select;
    PK pk;
    int permissionCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        delete = new Delete(getApplicationContext());
        select = new Select(getApplicationContext());

        txtUsuario = findViewById(R.id.edittext_User);
        txtPassw = findViewById(R.id.edittext_Password);

        fab_connections = findViewById(R.id.fab_connections);

        for (int i = 0; i < PERMISSIONS.length; i++) {
            permissionCheck += ContextCompat.checkSelfPermission(getApplicationContext(),
                    PERMISSIONS[i]);
        }

        if (permissionCheck != 0) {
            alertView();
        }

        /*
        Si ya hay un regristo
        * el valor del url_host sera el de la base de datos,
        * de lo contrario tomaremos el valor inicial
        * */

        try {
            if(select.getURL().isEmpty())
            {
                url_host = "http://158.69.225.115:8080";
            }
            else
            {
                url_host = select.getURL();
            }
        }catch (Exception e){
            System.out.println("Caemos en catch " + e.getMessage());
        }

        url_server = (url_host + url_ruta);

        m_setListener();

    }

    public void test(View view) {

        permissionCheck = 0;

        for (int i = 0; i < PERMISSIONS.length; i++) {
            permissionCheck += ContextCompat.checkSelfPermission(getApplicationContext(),
                    PERMISSIONS[i]);
        }

        if (permissionCheck != 0)
        {
            alertView();
        }
        else
            {
                TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myIMEI = mTelephony.getDeviceId();

                if (!txtUsuario.getText().toString().isEmpty() && !txtPassw.getText().toString().isEmpty() && !myIMEI.isEmpty())
                {
                    InicioSesionV2 task1 = new InicioSesionV2();
                    task1.execute(txtUsuario.getText().toString(), txtPassw.getText().toString(), myIMEI);
                }
                else
                {
                    Toast.makeText(this, "Debe ingresar correctamente los datos", Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void m_setListener()
    {
        fab_connections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Connections.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private class InicioSesionV2 extends AsyncTask<String,String,Integer> {

        ProgressDialog mSpinnerProgress;
        public InicioSesionV2() {

            mSpinnerProgress = new ProgressDialog(MainActivity.this);
            mSpinnerProgress.setIndeterminate(true);
            mSpinnerProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerProgress.setMessage("Iniciando Sesión");
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

            return iniciarSesion(params[0], params[1], params[2]);
        }

        private int iniciarSesion(String usuario, String password, String imei) {
            int result = 0;
            try
            {
                // Create http cliient object to send request to server
                HttpClient Client = new DefaultHttpClient();

                // Create URL string
                String url_param = "login?usuario=" + usuario + "&password=" + password + "&imei=" + imei;

                String SetServerString = "";

                // Create Request to server and get response
                HttpGet httpget = new HttpGet(url_server+url_param);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = Client.execute(httpget, responseHandler);

                JSONObject jsonR = new JSONObject(SetServerString);

                if(jsonR.get("status").toString().equals("A"))
                {
                    result = 1;
                    jparam = new JSONObject(jsonR.get("parameters").toString());
                    jsonparam = "{";
                    jsonparam += "\"dbName\":\"" + BASE_NAME + "\",";
                    jsonparam += "\"urlSrv\":\"" + url_server + "\",";
                    jsonparam += "\"idUsuario\":\"" + jparam.get("idUsuario").toString() + "\",";
                    jsonparam += "\"idPerfil\":\"" + jparam.get("idPerfil").toString() + "\",";
                    jsonparam += "\"nombre\":\"" + jparam.get("nombre").toString() + "\",";
                    jsonparam += "\"idEmpresa\":\"" + jparam.get("idEmpresa").toString() + "\",";
                    jsonparam += "\"almacen\":\"" + jparam.get("almacen").toString() + "\",";
                    jsonparam += "\"idAlmacen\":\"" + jparam.get("idAlmacen").toString() + "\",";
                    jsonparam += "\"impresion\":\""+ jparam.get("imprime").toString()+ "\"";
                    jsonparam += "}";

                    pk = new PK(BASE_NAME, url_server,
                            jparam.get("idUsuario").toString(), jparam.get("idPerfil").toString(),
                            jparam.get("nombre").toString(), jparam.get("idEmpresa").toString(),
                            jparam.get("almacen").toString(), jparam.get("idAlmacen").toString(),
                            jparam.get("imprime").toString());

                }
                else
                {
                    result = 2;
                    msg =  jsonR.get("description").toString();
                }
            }
            catch (Exception ex)
            {
                System.out.println(" catch " + usuario + password + imei + " error " + ex.toString());
            }
            finally
            {

            }

            return result;
        }

        protected void onPostExecute(Integer result){

            switch(result){
                case 1:
                    delete.deleteCatalogosFile();
                    Descarga_Informacion descargarInfo = new Descarga_Informacion(getApplicationContext(), MainActivity.this);
                    descargarInfo.Descargar_Catalogo(pk, "login");
                    /*Intent intent = new Intent(MainActivity.this, Menu_Principal.class);
                    //intent.putExtra("json", jparam.toString());
                    intent.putExtra("pk", pk);
                    startActivity(intent)*/;
                    Toast.makeText(MainActivity.this, "Autenticación Exitosa ", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    break;

            }
            mSpinnerProgress.dismiss();
        }

    }

    public boolean requestPermission(String... permissions) {
        final List<String> permissionsList = new ArrayList<>();

        for (String perm : permissions) {
            addPermission(permissionsList, perm);
        }

        if (permissionsList.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), MULTIPLE_PERMISSION_REQUEST);
            }
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), MULTIPLE_PERMISSION_REQUEST);
            return false;
        } else {
            return true;
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    private void alertView() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setInverseBackgroundForced(true);
        alertDialog.setTitle("Bienvenido");
        alertDialog.setMessage("Para garantizar el correcto funcionamiento, es necesario dar permisos a la aplicación.");
        alertDialog.setIcon(R.drawable.heineken_logo);
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Permisos", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermission(PERMISSIONS);
            }
        });

        alertDialog.show();
    }

}
