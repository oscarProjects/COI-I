package com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrador.heinekenandroid.R;
import com.heineken.greendaoapp.db.Salidas;

import java.util.List;

public class RegistrosAdapter extends BaseAdapter {

    Activity activity;
    List<Salidas> items;

    public RegistrosAdapter(Activity activity, List<Salidas> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        Salidas salidas = (Salidas) getItem(i);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_listview, null);
        }

        TextView textView_set_sku = v.findViewById(R.id.textView_set_sku);
        TextView textView_set_codigo = v.findViewById(R.id.textView_set_codigo);
        TextView textView_set_idum = v.findViewById(R.id.textView_set_idum);
        TextView textView_set_cantidad = v.findViewById(R.id.textView_set_cantidad);
        // Populate the data into the template view using the data object

        textView_set_sku.setText("Sku: " + salidas.getSku());
        textView_set_codigo.setText("Codigo: " + salidas.getCodigo());
        textView_set_idum.setText("Id UM" + salidas.getIdum());
        textView_set_cantidad.setText("Cantidad:" + salidas.getCantidad());

        return v;
    }
}