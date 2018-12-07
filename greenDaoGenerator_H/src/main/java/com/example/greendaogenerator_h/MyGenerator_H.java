package com.example.greendaogenerator_h;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator_H {

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.heineken.greendaoapp.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addConfigEntities(schema);
        addLecturasEntities(schema);
        addLotesEntities(schema);
        addProductosEntities(schema);
        addRutasEntities(schema);
        addSalidasEntities(schema);
        addSeccionesEntities(schema);
        addUMEntities(schema);
        addFamiliaEntities(schema);
        addPropiedadesEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addConfigEntities(final Schema schema) {
        Entity configuracion = schema.addEntity("Configuracion");
        configuracion.addIdProperty().primaryKey().autoincrement();
        configuracion.addStringProperty("_id_").notNull();
        configuracion.addStringProperty("srv");
        configuracion.addStringProperty("tipo");
        configuracion.addStringProperty("ip");
        configuracion.addStringProperty("device");
        configuracion.addStringProperty("nolabels");
        return configuracion;
    }

    private static Entity addLecturasEntities(final Schema schema) {
        Entity lectura = schema.addEntity("Lecturas");
        lectura.addIdProperty().primaryKey().autoincrement();
        lectura.addStringProperty("id_lectura").notNull();
        lectura.addStringProperty("sku");
        lectura.addStringProperty("cb");
        lectura.addStringProperty("descripcion");
        lectura.addStringProperty("id_um");
        lectura.addStringProperty("um");
        lectura.addStringProperty("cantidad");
        return lectura;
    }
    private static Entity addLotesEntities(final Schema schema) {
        Entity lotes = schema.addEntity("Lotes");
        lotes.addIdProperty().primaryKey().autoincrement();
        lotes.addStringProperty("_id_").notNull();
        lotes.addStringProperty("lote");
        return lotes;
    }
    private static Entity addProductosEntities(final Schema schema) {
        Entity productos = schema.addEntity("Productos");
        productos.addIdProperty().primaryKey().autoincrement();
        productos.addStringProperty("sku").notNull();
        productos.addStringProperty("cb");
        productos.addStringProperty("ml");
        productos.addStringProperty("descripcion");
        return productos;
    }
    private static Entity addRutasEntities(final Schema schema) {
        Entity rutas = schema.addEntity("Rutas");
        rutas.addIdProperty().primaryKey().autoincrement();
        rutas.addStringProperty("id_ruta").notNull();
        rutas.addStringProperty("ruta");
        return rutas;
    }
    private static Entity addSalidasEntities(final Schema schema) {
        Entity salidas = schema.addEntity("Salidas");
        salidas.addIdProperty().primaryKey().autoincrement();
        salidas.addStringProperty("_id_").notNull();
        salidas.addStringProperty("sku");
        salidas.addStringProperty("idum");
        salidas.addStringProperty("cantidad");
        salidas.addStringProperty("codigo");
        return salidas;
    }
    private static Entity addSeccionesEntities(final Schema schema) {
        Entity secciones = schema.addEntity("Secciones");
        secciones.addIdProperty().primaryKey().autoincrement();
        secciones.addStringProperty("id_seccion").notNull();
        secciones.addStringProperty("seccion");
        return secciones;
    }
    private static Entity addUMEntities(final Schema schema) {
        Entity um = schema.addEntity("UM");
        um.addIdProperty().primaryKey().autoincrement();
        um.addStringProperty("id_um").notNull();
        um.addStringProperty("um");
        return um;
    }

    private static Entity addFamiliaEntities(final Schema schema) {
        Entity familia = schema.addEntity("Familia");
        familia.addIdProperty().primaryKey().autoincrement();
        familia.addStringProperty("idcatalogo_familia").notNull();
        familia.addStringProperty("identificador");
        familia.addStringProperty("descripcion");
        return familia;
    }

    private static Entity addPropiedadesEntities(final Schema schema) {
        Entity propiedades = schema.addEntity("Propiedades");
        propiedades.addIdProperty().primaryKey().autoincrement();
        propiedades.addIntProperty("id_url").notNull();
        propiedades.addStringProperty("url_server");
        return propiedades;
    }

}
