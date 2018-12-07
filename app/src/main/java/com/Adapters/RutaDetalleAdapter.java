package com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.heinekenandroid.Extras.Ruta_Detalle;
import com.example.administrador.heinekenandroid.R;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.ArrayList;

public class RutaDetalleAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Ruta_Detalle> items;
    int contador = 0;

    public RutaDetalleAdapter(Activity activity, ArrayList<Ruta_Detalle> items) {
        this.activity = activity;
        this.items = items;
        this.contador = 0;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Ruta_Detalle> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView textView_clave_sku;
        TextView textView_desc_sku;
        TextView textView_desc_um;
        TextView textView_cantidad;
        TextView textView_cantidad_detalle;
        TextView textView_familia;

        public ViewHolder(View view) {
            textView_clave_sku = view.findViewById(R.id.textView_clave_sku);
            textView_desc_sku = view.findViewById(R.id.textView_desc_sku);
            textView_desc_um = view.findViewById(R.id.textView_desc_um);
            textView_cantidad = view.findViewById(R.id.textView_cantidad);
            textView_cantidad_detalle = view.findViewById(R.id.textView_cantidad_detalle);
            textView_familia = view.findViewById(R.id.textView_familia);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_ruta_detalle, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ruta_Detalle r_d = items.get(position);

        viewHolder.textView_clave_sku.setText("Sku: " + r_d.getId());

        viewHolder.textView_desc_sku.setText(r_d.getSku());

        viewHolder.textView_desc_um.setText("Unidad de Medida: " + r_d.getIdum());

        viewHolder.textView_cantidad.setText("Cantidad: " + r_d.getCantidad());

        viewHolder.textView_cantidad_detalle.setText("Capturados: " + r_d.getCantidadDetalle());

        viewHolder.textView_familia.setText("Familia: " + r_d.getFamilia());


        //final Button b1 = activity.findViewById(R.id.btn_guardar_route_detail);

            if(r_d.getCantidad().equals(r_d.getCantidadDetalle()))
            {
                convertView.setBackgroundColor(Color.parseColor("#DAF7A6"));
            }
            else
            {
                convertView.setBackgroundColor(Color.parseColor("#FF5733"));
            }

        /*if(contador < items.size()) {
            contador ++;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    b1.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            contador --;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    b1.setVisibility(View.VISIBLE);
                }
            });
        }
        */

        return convertView;
    }
}