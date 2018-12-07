package com.example.administrador.heinekenandroid.dbQuerys;

import android.content.Context;
import android.database.Cursor;
import com.heineken.greendaoapp.db.*;

import java.util.List;

public class Select {

    Context context;

    Sesiones sesiones = new Sesiones();

    public Select(Context context){
        this.context=context;
    }

    public String getDescripcionSku(String SKU){
        DaoSession daoSession=sesiones.Iniciar_Session(this.context);
        ProductosDao productosDao = daoSession.getProductosDao();
        Cursor cursor = productosDao.getDatabase().rawQuery(
                "SELECT DISTINCT " + ProductosDao.Properties.Descripcion.columnName
                        + " FROM " + ProductosDao.TABLENAME
                        + " WHERE " + ProductosDao.Properties.Sku.columnName + " = '" + SKU + "'",null);
        String descripcion = "";
        if(cursor.moveToFirst()){
            do{
                descripcion = cursor.getString(cursor.getColumnIndex(ProductosDao.Properties.Descripcion.columnName));
            }while (cursor.moveToNext());
            cursor.close();
        }
        sesiones.Cerrar_Session();
        return descripcion;
    }

    public List<Secciones> getSecciones(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SeccionesDao seccionesDao = daoSession.getSeccionesDao();
        List<Secciones> list = seccionesDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<UM> getUm(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        UMDao umDao = daoSession.getUMDao();
        List<UM> list = umDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<Rutas> getRutas(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        RutasDao rutasDao = daoSession.getRutasDao();
        List<Rutas> list = rutasDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<Salidas> getSalidas(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SalidasDao salidasDao = daoSession.getSalidasDao();
        List<Salidas> list = salidasDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<Lotes> getLotes(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        LotesDao lotesDao = daoSession.getLotesDao();
        List<Lotes> list = lotesDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<Familia> getFamilias(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        FamiliaDao familiaDao = daoSession.getFamiliaDao();
        List<Familia> list = familiaDao.queryBuilder().list();
        sesiones.Cerrar_Session();
        return list;
    }

    public String getCodBarSku(String SKU){
        DaoSession daoSession=sesiones.Iniciar_Session(this.context);
        ProductosDao productosDao = daoSession.getProductosDao();
        Cursor cursor = productosDao.getDatabase().rawQuery(
                "SELECT DISTINCT " + ProductosDao.Properties.Cb.columnName
                        + " FROM " + ProductosDao.TABLENAME
                        + " WHERE " + ProductosDao.Properties.Sku.columnName + " = '" + SKU + "'",null);
        String descripcion = "";
        if(cursor.moveToFirst()){
            do{
                descripcion = cursor.getString(cursor.getColumnIndex(ProductosDao.Properties.Cb.columnName));
            }while (cursor.moveToNext());
            cursor.close();
        }
        sesiones.Cerrar_Session();
        return descripcion;
    }

    public List<UM> getUmTarimas(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        UMDao umDao = daoSession.getUMDao();
        List<UM> list = umDao.queryBuilder()
                .where(UMDao.Properties.Um.eq("TARIMA"))
                .list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<UM> getUmOthers(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        UMDao umDao = daoSession.getUMDao();
        List<UM> list = umDao.queryBuilder()
                .where(UMDao.Properties.Um.in("CAJA", "PIEZA"))
                .list();
        sesiones.Cerrar_Session();
        return list;
    }

    public List<Secciones> getSeccionesDevoluciones(){
        DaoSession daoSession = sesiones.Iniciar_Session(this.context);
        SeccionesDao seccionesDao = daoSession.getSeccionesDao();
        List<Secciones> list = seccionesDao.queryBuilder()
                .where(SeccionesDao.Properties.Seccion.in("Lote", "Merma", "Picking"))
                .list();
        sesiones.Cerrar_Session();
        return list;
    }

    public String getURL(){
        DaoSession daoSession=sesiones.Iniciar_Session(this.context);
        PropiedadesDao propiedadesDao = daoSession.getPropiedadesDao();
        Cursor cursor = propiedadesDao.getDatabase().rawQuery(
                "SELECT " + PropiedadesDao.Properties.Url_server.columnName
                        + " FROM " + PropiedadesDao.TABLENAME,null);
        String url = "";
        if(cursor.moveToFirst()){
            do{
                url = cursor.getString(cursor.getColumnIndex(PropiedadesDao.Properties.Url_server.columnName));
            }while (cursor.moveToNext());
            cursor.close();
        }
        sesiones.Cerrar_Session();
        return url;
    }
}
