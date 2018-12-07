package com.example.administrador.heinekenandroid.Extras;

import android.widget.Spinner;

import com.heineken.greendaoapp.db.Propiedades;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Variables_Generales {

    public static final String BASE_NAME="AcaoPissaDB.db";

    //public static String url_server = "http://158.69.225.115:8080/CeappWS/service/rest/";

    //public static String url_host = "http://158.69.225.115:8080";

    public static String url_host = "http://158.69.225.115:8080";

    public static String url_ruta = "/CeappWS/service/rest/";

    public static String url_server = (url_host+url_ruta);

    public static final String inicializarItem = "-- --";

    public static int TotalCatalogos = 6;

    public static String Direcciones(int num){
        String valor="";
        switch (num){
            case 0:
                valor="get_secciones";
                break;
            case 1:
                valor="get_um";
                break;
            case 2:
                valor="get_productos";
                break;
            case 3:
                valor="get_rutas";
                break;
            case 4:
                valor="get_lotes";
                break;
            case 5:
                valor="get_familias";
                break;
        }

        return valor;
    }

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatOrdinal(int year, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_YEAR, day);
        Date date = cal.getTime();
        return formatter.format(date);
    }

    public static int obtenerPosicionItem(Spinner spinner, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }
}
