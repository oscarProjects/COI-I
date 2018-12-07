package com.heineken.greendaoapp.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "CONFIGURACION".
 */
@Entity
public class Configuracion {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String _id_;
    private String srv;
    private String tipo;
    private String ip;
    private String device;
    private String nolabels;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Configuracion() {
    }

    public Configuracion(Long id) {
        this.id = id;
    }

    @Generated
    public Configuracion(Long id, String _id_, String srv, String tipo, String ip, String device, String nolabels) {
        this.id = id;
        this._id_ = _id_;
        this.srv = srv;
        this.tipo = tipo;
        this.ip = ip;
        this.device = device;
        this.nolabels = nolabels;
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

    public String getSrv() {
        return srv;
    }

    public void setSrv(String srv) {
        this.srv = srv;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getNolabels() {
        return nolabels;
    }

    public void setNolabels(String nolabels) {
        this.nolabels = nolabels;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}