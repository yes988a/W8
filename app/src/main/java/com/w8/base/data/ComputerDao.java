package com.w8.base.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPUTER".
*/
public class ComputerDao extends AbstractDao<Computer, Long> {

    public static final String TABLENAME = "COMPUTER";

    /**
     * Properties of entity Computer.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Acc = new Property(1, String.class, "acc", false, "ACC");
        public final static Property Ipp = new Property(2, String.class, "ipp", false, "IPP");
    }


    public ComputerDao(DaoConfig config) {
        super(config);
    }
    
    public ComputerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPUTER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"ACC\" TEXT NOT NULL UNIQUE ," + // 1: acc
                "\"IPP\" TEXT NOT NULL );"); // 2: ipp
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPUTER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Computer entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getAcc());
        stmt.bindString(3, entity.getIpp());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Computer entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getAcc());
        stmt.bindString(3, entity.getIpp());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Computer readEntity(Cursor cursor, int offset) {
        Computer entity = new Computer( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getString(offset + 1), // acc
            cursor.getString(offset + 2) // ipp
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Computer entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAcc(cursor.getString(offset + 1));
        entity.setIpp(cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Computer entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Computer entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Computer entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}