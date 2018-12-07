package com.example.administrador.heinekenandroid.dbQuerys;

import android.content.Context;

import com.heineken.greendaoapp.db.*;

public class Insert {

    Context context;

    Sesiones sesiones=new Sesiones();

    public Insert(Context context){
        this.context=context;
    }

    public void insertConfiguracion(Configuracion configuracion){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        ConfiguracionDao configDao = daoSession.getConfiguracionDao();
            configDao.insert(configuracion);
        sesiones.Cerrar_Session();
    }

    public void insertLecturas(Lecturas lecturas){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        LecturasDao lecturasDao = daoSession.getLecturasDao();
            lecturasDao.insert(lecturas);
        sesiones.Cerrar_Session();
    }

    public void insertLotes(Lotes lotes){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        LotesDao lotesDao = daoSession.getLotesDao();
            lotesDao.insert(lotes);
        sesiones.Cerrar_Session();
    }

    public void insertProductos(Productos productos){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        ProductosDao productosDao = daoSession.getProductosDao();
            productosDao.insert(productos);
        sesiones.Cerrar_Session();
    }

    public void insertRutas(Rutas rutas){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        RutasDao rutasDao = daoSession.getRutasDao();
            rutasDao.insert(rutas);
        sesiones.Cerrar_Session();
    }

    public void insertSalidas(Salidas salidas){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SalidasDao salidasDao = daoSession.getSalidasDao();
            salidasDao.insert(salidas);
        sesiones.Cerrar_Session();
    }

    public void insertSecciones (Secciones secciones){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SeccionesDao seccionesDao = daoSession.getSeccionesDao();
        seccionesDao.insert(secciones);
        sesiones.Cerrar_Session();
    }

    public void insertUM(UM um){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        UMDao umDao = daoSession.getUMDao();
            umDao.insert(um);
        sesiones.Cerrar_Session();
    }

    public void insertFamilias(Familia familia){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        FamiliaDao familiaDao = daoSession.getFamiliaDao();
        familiaDao.insert(familia);
        sesiones.Cerrar_Session();
    }

    public void insertURL(Propiedades propiedades){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        PropiedadesDao propiedadesDao = daoSession.getPropiedadesDao();
        propiedadesDao.insert(propiedades);
        sesiones.Cerrar_Session();
    }


}
