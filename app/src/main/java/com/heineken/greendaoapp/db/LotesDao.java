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
 * DAO for table "LOTES".
*/
public class LotesDao extends AbstractDao<Lotes, Long> {

    public static final String TABLENAME = "LOTES";

    /**
     * Properties of entity Lotes.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property _id_ = new Property(1, String.class, "_id_", false, "_ID_");
        public final static Property Lote = new Property(2, String.class, "lote", false, "LOTE");
    }


    public LotesDao(DaoConfig config) {
        super(config);
    }
    
    public LotesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOTES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"_ID_\" TEXT NOT NULL ," + // 1: _id_
                "\"LOTE\" TEXT);"); // 2: lote
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOTES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Lotes entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.get_id_());
 
        String lote = entity.getLote();
        if (lote != null) {
            stmt.bindString(3, lote);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Lotes entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.get_id_());
 
        String lote = entity.getLote();
        if (lote != null) {
            stmt.bindString(3, lote);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Lotes readEntity(Cursor cursor, int offset) {
        Lotes entity = new Lotes( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // _id_
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // lote
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Lotes entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.set_id_(cursor.getString(offset + 1));
        entity.setLote(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Lotes entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Lotes entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Lotes entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
