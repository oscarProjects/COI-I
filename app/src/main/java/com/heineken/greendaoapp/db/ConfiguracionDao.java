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
 * DAO for table "CONFIGURACION".
*/
public class ConfiguracionDao extends AbstractDao<Configuracion, Long> {

    public static final String TABLENAME = "CONFIGURACION";

    /**
     * Properties of entity Configuracion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property _id_ = new Property(1, String.class, "_id_", false, "_ID_");
        public final static Property Srv = new Property(2, String.class, "srv", false, "SRV");
        public final static Property Tipo = new Property(3, String.class, "tipo", false, "TIPO");
        public final static Property Ip = new Property(4, String.class, "ip", false, "IP");
        public final static Property Device = new Property(5, String.class, "device", false, "DEVICE");
        public final static Property Nolabels = new Property(6, String.class, "nolabels", false, "NOLABELS");
    }


    public ConfiguracionDao(DaoConfig config) {
        super(config);
    }
    
    public ConfiguracionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONFIGURACION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"_ID_\" TEXT NOT NULL ," + // 1: _id_
                "\"SRV\" TEXT," + // 2: srv
                "\"TIPO\" TEXT," + // 3: tipo
                "\"IP\" TEXT," + // 4: ip
                "\"DEVICE\" TEXT," + // 5: device
                "\"NOLABELS\" TEXT);"); // 6: nolabels
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONFIGURACION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Configuracion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.get_id_());
 
        String srv = entity.getSrv();
        if (srv != null) {
            stmt.bindString(3, srv);
        }
 
        String tipo = entity.getTipo();
        if (tipo != null) {
            stmt.bindString(4, tipo);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(5, ip);
        }
 
        String device = entity.getDevice();
        if (device != null) {
            stmt.bindString(6, device);
        }
 
        String nolabels = entity.getNolabels();
        if (nolabels != null) {
            stmt.bindString(7, nolabels);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Configuracion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.get_id_());
 
        String srv = entity.getSrv();
        if (srv != null) {
            stmt.bindString(3, srv);
        }
 
        String tipo = entity.getTipo();
        if (tipo != null) {
            stmt.bindString(4, tipo);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(5, ip);
        }
 
        String device = entity.getDevice();
        if (device != null) {
            stmt.bindString(6, device);
        }
 
        String nolabels = entity.getNolabels();
        if (nolabels != null) {
            stmt.bindString(7, nolabels);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Configuracion readEntity(Cursor cursor, int offset) {
        Configuracion entity = new Configuracion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // _id_
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // srv
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // tipo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ip
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // device
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // nolabels
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Configuracion entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.set_id_(cursor.getString(offset + 1));
        entity.setSrv(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTipo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIp(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDevice(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setNolabels(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Configuracion entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Configuracion entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Configuracion entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}