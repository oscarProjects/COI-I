package com.heineken.greendaoapp.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "SALIDAS".
 */
@Entity
public class Salidas {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String _id_;
    private String sku;
    private String idum;
    private String cantidad;
    private String codigo;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Salidas() {
    }

    public Salidas(Long id) {
        this.id = id;
    }

    @Generated
    public Salidas(Long id, String _id_, String sku, String idum, String cantidad, String codigo) {
        this.id = id;
        this._id_ = _id_;
        this.sku = sku;
        this.idum = idum;
        this.cantidad = cantidad;
        this.codigo = codigo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String get_id_() {
        return _id_;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void set_id_(@NotNull String _id_) {
        this._id_ = _id_;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}