package com.example.administrador.heinekenandroid.dbQuerys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.heinekenandroid.Extras.Variables_Generales;
import com.heineken.greendaoapp.db.DaoMaster;
import com.heineken.greendaoapp.db.DaoSession;

public class Sesiones {
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoSession daoSession;

    public DaoSession Iniciar_Session(Context context){
        helper = new DaoMaster.DevOpenHelper(context, Variables_Generales.BASE_NAME, null);
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        return daoSession;
    }

    public void Cerrar_Session(){
        helper.close();
        db.close();
        daoSession.clear();
    }
}
