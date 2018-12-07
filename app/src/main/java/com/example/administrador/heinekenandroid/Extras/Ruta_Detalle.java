package com.example.administrador.heinekenandroid.Extras;

import java.io.Serializable;

public class Ruta_Detalle implements Serializable
{
    private String id;
    private String sku;
    private String idum;
    private String cantidad;
    private String cantidadDetalle;
    private String familia;
    private String idFamilia;

    public Ruta_Detalle() {
        super();
    }

    public Ruta_Detalle(String id, String sku, String idum, String cantidad, String cantidadDetalle, String familia, String idFamilia) {
        super();
        this.id = id;
        this.sku = sku;
        this.idum = idum;
        this.cantidad = cantidad;
        this.cantidadDetalle = cantidadDetalle;
        this.familia = familia;
        this.idFamilia = idFamilia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getIdum() {
        return idum;
    }

    public void setIdum(String idum) {
        this.idum = idum;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCantidadDetalle() {
        return cantidadDetalle;
    }

    public void setCantidadDetalle(String cantidadDetalle) {
        this.cantidadDetalle = cantidadDetalle;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getId_familia() {
        return idFamilia;
    }

    public void setId_familia(String idFamilia) {
        this.idFamilia = idFamilia;
    }
}
