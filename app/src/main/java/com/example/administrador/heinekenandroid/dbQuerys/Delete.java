package com.example.administrador.heinekenandroid.dbQuerys;

import android.content.Context;

import com.example.administrador.heinekenandroid.Extras.Variables_Generales;
import com.heineken.greendaoapp.db.DaoSession;
import com.heineken.greendaoapp.db.Salidas;
import com.heineken.greendaoapp.db.SalidasDao;

import java.util.List;

public class Delete
{

    Context context;

    Sesiones sesiones = new Sesiones();

    public Delete(Context context)
    {
        this.context=context;
    }

    public void deleteCatalogosFile()
    {
        Variables_Generales var=new Variables_Generales();
        context.deleteDatabase(var.BASE_NAME);
    }

    public void deleteSalidas()
    {
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SalidasDao salidasDao = daoSession.getSalidasDao();
        List<Salidas> list = salidasDao.queryBuilder().list();
        for(int i=0;i<list.size();i++){
            salidasDao.delete(list.get(i));
        }
        sesiones.Cerrar_Session();
    }
}