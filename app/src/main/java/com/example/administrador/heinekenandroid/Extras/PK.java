package com.example.administrador.heinekenandroid.Extras;

import android.app.Activity;
import android.content.Context;

import com.example.administrador.heinekenandroid.Menu_Principal;

import java.io.Serializable;

public class PK implements Serializable {

    public String dbName;
    public String urlSrv;
    public String idUsuario;
    public String idPerfil;
    public String nombre;
    public String idEmpresa;
    public String almacen;
    public String idAlmacen;
    public String impresion;

    public PK (String dbName, String urlSrv, String idUsuario,
               String idPerfil, String nombre, String idEmpresa,
               String almacen, String idAlmacen, String impresion)
    {
        this.dbName = dbName;
        this.urlSrv = urlSrv;
        this.idUsuario = idUsuario;
        this.idPerfil = idPerfil;
        this.nombre = nombre;
        this.idEmpresa = idEmpresa;
        this.almacen = almacen;
        this.idAlmacen = idAlmacen;
        this.impresion = impresion;
    }

}
