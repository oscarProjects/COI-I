package com.heineken.greendaoapp.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "RUTAS".
 */
@Entity
public class Rutas {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String id_ruta;
    private String ruta;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Rutas() {
    }

    public Rutas(Long id) {
        this.id = id;
    }

    @Generated
    public Rutas(Long id, String id_ruta, String ruta) {
        this.id = id;
        this.id_ruta = id_ruta;
        this.ruta = ruta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getId_ruta() {
        return id_ruta;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId_ruta(@NotNull String id_ruta) {
        this.id_ruta = id_ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
