package com.heineken.greendaoapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "RUTAS".
*/
public class RutasDao extends AbstractDao<Rutas, Long> {

    public static final String TABLENAME = "RUTAS";

    /**
     * Properties of entity Rutas.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Id_ruta = new Property(1, String.class, "id_ruta", false, "ID_RUTA");
        public final static Property Ruta = new Property(2, String.class, "ruta", false, "RUTA");
    }


    public RutasDao(DaoConfig config) {
        super(config);
    }
    
    public RutasDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"RUTAS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ID_RUTA\" TEXT NOT NULL ," + // 1: id_ruta
                "\"RUTA\" TEXT);"); // 2: ruta
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RUTAS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Rutas entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getId_ruta());
 
        String ruta = entity.getRuta();
        if (ruta != null) {
            stmt.bindString(3, ruta);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Rutas entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getId_ruta());
 
        String ruta = entity.getRuta();
        if (ruta != null) {
            stmt.bindString(3, ruta);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Rutas readEntity(Cursor cursor, int offset) {
        Rutas entity = new Rutas( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // id_ruta
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // ruta
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Rutas entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId_ruta(cursor.getString(offset + 1));
        entity.setRuta(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Rutas entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Rutas entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Rutas entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
