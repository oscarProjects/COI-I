package com.example.administrador.heinekenandroid.dbQuerys;

import android.content.Context;

import com.heineken.greendaoapp.db.DaoSession;
import com.heineken.greendaoapp.db.Propiedades;
import com.heineken.greendaoapp.db.PropiedadesDao;

import java.util.Iterator;
import java.util.List;

public class Update {

    Context context;

    Sesiones sesiones=new Sesiones();

    public Update(Context context){
        this.context=context;
    }

    public void updateURL(String url){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        PropiedadesDao propiedadesDao = daoSession.getPropiedadesDao();
        List<Propiedades> propiedadesList = propiedadesDao.queryBuilder().list();
        Iterator<Propiedades> propiedadesIterator = propiedadesList.iterator();
        while(propiedadesIterator.hasNext()){
            Propiedades aux = propiedadesIterator.next();
            aux.setUrl_server(url);
            propiedadesDao.update(aux);
        }
        sesiones.Cerrar_Session();
    }

}
