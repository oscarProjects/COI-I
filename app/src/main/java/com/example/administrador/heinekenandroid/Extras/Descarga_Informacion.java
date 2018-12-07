package com.example.administrador.heinekenandroid.Extras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Menu_Principal;
import com.example.administrador.heinekenandroid.dbQuerys.Insert;
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

import static com.example.administrador.heinekenandroid.Extras.Variables_Generales.url_server;

public class Descarga_Informacion {

    Context context;
    Activity activity;
    String url_param;

    PK pk;
    String ubicacion;

    public Descarga_Informacion(Context context,Activity activity){
        this.context=context;
        this.activity=activity;

    }

    public void Descargar_Catalogo(PK pk, String ubicacion){
        DescargaCatalogos descargaCatalogosV2=new DescargaCatalogos();
        descargaCatalogosV2.execute(pk.idEmpresa, pk.idAlmacen);
        this.pk = pk;
        this.ubicacion = ubicacion;
    }

    private class DescargaCatalogos extends AsyncTask<String,String,Integer> {

        Insert insert = new Insert(activity.getApplicationContext());

        ProgressDialog mSpinnerProgress;
        public DescargaCatalogos() {

            mSpinnerProgress = new ProgressDialog(activity);
            mSpinnerProgress.setIndeterminate(true);
            mSpinnerProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mSpinnerProgress.setMessage("Descargando Catálogos");
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
            return descargaCatalogos(params[0], params[1]);
        }

        private int descargaCatalogos(String id_Empresa, String id_Almacen) {
            int result = 0;

            for(int i = 0; i< Variables_Generales.TotalCatalogos; i++){

                try
                {
                    // Create http cliient object to send request to server
                    HttpClient Client = new DefaultHttpClient();

                    if(Variables_Generales.Direcciones(i).equals("get_secciones") || Variables_Generales.Direcciones(i).equals("get_um")
                            || Variables_Generales.Direcciones(i).equals("get_familias"))
                    {
                        // Create URL string
                        url_param = Variables_Generales.Direcciones(i);
                    }
                    else
                    {
                        // Create URL string
                        url_param = Variables_Generales.Direcciones(i)+"?id_empresa=" + id_Empresa + "&id_almacen=" + id_Almacen;
                    }
                    String SetServerString = "";

                    // Create Request to server and get response
                    HttpGet httpget = new HttpGet(url_server+url_param);

                    System.out.println("URL catalogos " + (url_server+url_param));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    SetServerString = Client.execute(httpget, responseHandler);

                    JSONArray jsonR = new JSONArray(SetServerString);
                    for(int x = 0; x < jsonR.length(); x++){

                        JSONObject subObj = jsonR.getJSONObject(x);

                        switch (i){

                            case 0:
                                //OK
                                Secciones s = new Secciones();
                                s.setId_seccion(subObj.get("id_seccion").toString());
                                s.setSeccion(subObj.get("seccion").toString());
                                insert.insertSecciones(s);
                                break;
                            case 1:
                                //OK
                                UM u = new UM();
                                u.setId_um(subObj.get("id_um").toString());
                                u.setUm(subObj.get("um").toString());
                                insert.insertUM(u);
                                break;
                            case 2:
                                //OK
                                Productos p = new Productos();
                                p.setSku(subObj.get("sku").toString());
                                p.setCb(subObj.get("cb").toString());
                                p.setMl(subObj.get("ml").toString());
                                p.setDescripcion(subObj.get("descripcion").toString());
                                insert.insertProductos(p);
                                break;
                            case 3:
                                //OK
                                Rutas r = new Rutas();
                                r.setId_ruta(subObj.get("id_ruta").toString());
                                r.setRuta(subObj.get("ruta").toString());
                                insert.insertRutas(r);
                                break;
                            case 4:
                                //OK
                                Lotes l = new Lotes();
                                l.set_id_(subObj.get("id").toString());
                                l.setLote(subObj.get("lote").toString());
                                insert.insertLotes(l);
                                break;
                            case 5:
                                //OK
                                Familia f = new Familia();
                                f.setIdcatalogo_familia(subObj.get("id").toString());
                                f.setIdentificador(subObj.get("identificador").toString());
                                f.setDescripcion(subObj.get("descripcion").toString());
                                insert.insertFamilias(f);
                                break;
                        }
                        result = 1;
                    }
                }
                catch (final Exception ex)
                {
                    result = 0;
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity, "Revise la conexion " +


                                    ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    System.out.println(" message " + ex.getMessage());
                    break;
                }
                finally
                {
                }
            }

            return result;
        }

        protected void onPostExecute(Integer result){
            switch (result)
            {
                case 0:
                    Toast.makeText(activity.getApplicationContext(), "Hay un Error en la Descarga de Informacion.", Toast.LENGTH_SHORT).show();
                break;
                case 1:
                    Toast.makeText(activity.getApplicationContext(), "Descarga Exitosa", Toast.LENGTH_SHORT).show();
                    if(ubicacion.equals("login"))
                    {
                        Intent intent = new Intent(activity, Menu_Principal.class);
                        intent.putExtra("pk", pk);
                        activity.startActivity(intent);
                    }
                    break;
            }

            mSpinnerProgress.dismiss();
        }

    }

}
