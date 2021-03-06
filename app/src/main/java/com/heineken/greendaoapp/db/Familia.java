package com.heineken.greendaoapp.db;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table "FAMILIA".
 */
@Entity
public class Familia {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String idcatalogo_familia;
    private String identificador;
    private String descripcion;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public Familia() {
    }

    public Familia(Long id) {
        this.id = id;
    }

    @Generated
    public Familia(Long id, String idcatalogo_familia, String identificador, String descripcion) {
        this.id = id;
        this.idcatalogo_familia = idcatalogo_familia;
        this.identificador = identificador;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getIdcatalogo_familia() {
        return idcatalogo_familia;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setIdcatalogo_familia(@NotNull String idcatalogo_familia) {
        this.idcatalogo_familia = idcatalogo_familia;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
