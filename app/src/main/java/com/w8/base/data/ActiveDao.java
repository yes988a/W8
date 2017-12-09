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
 * DAO for table "ACTIVE".
*/
public class ActiveDao extends AbstractDao<Active, Long> {

    public static final String TABLENAME = "ACTIVE";

    /**
     * Properties of entity Active.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Uuid = new Property(1, String.class, "uuid", false, "UUID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Des = new Property(3, String.class, "des", false, "DES");
        public final static Property Btyp = new Property(4, Integer.class, "btyp", false, "BTYP");
        public final static Property Num = new Property(5, int.class, "num", false, "NUM");
        public final static Property Timstr = new Property(6, String.class, "timstr", false, "TIMSTR");
        public final static Property Tim = new Property(7, Long.class, "tim", false, "TIM");
    }


    public ActiveDao(DaoConfig config) {
        super(config);
    }
    
    public ActiveDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACTIVE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"UUID\" TEXT NOT NULL UNIQUE ," + // 1: uuid
                "\"TITLE\" TEXT NOT NULL ," + // 2: title
                "\"DES\" TEXT NOT NULL ," + // 3: des
                "\"BTYP\" INTEGER NOT NULL ," + // 4: btyp
                "\"NUM\" INTEGER NOT NULL ," + // 5: num
                "\"TIMSTR\" TEXT NOT NULL ," + // 6: timstr
                "\"TIM\" INTEGER NOT NULL );"); // 7: tim
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACTIVE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Active entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getUuid());
        stmt.bindString(3, entity.getTitle());
        stmt.bindString(4, entity.getDes());
        stmt.bindLong(5, entity.getBtyp());
        stmt.bindLong(6, entity.getNum());
        stmt.bindString(7, entity.getTimstr());
        stmt.bindLong(8, entity.getTim());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Active entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getUuid());
        stmt.bindString(3, entity.getTitle());
        stmt.bindString(4, entity.getDes());
        stmt.bindLong(5, entity.getBtyp());
        stmt.bindLong(6, entity.getNum());
        stmt.bindString(7, entity.getTimstr());
        stmt.bindLong(8, entity.getTim());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Active readEntity(Cursor cursor, int offset) {
        Active entity = new Active( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getString(offset + 1), // uuid
            cursor.getString(offset + 2), // title
            cursor.getString(offset + 3), // des
            cursor.getInt(offset + 4), // btyp
            cursor.getInt(offset + 5), // num
            cursor.getString(offset + 6), // timstr
            cursor.getLong(offset + 7) // tim
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Active entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUuid(cursor.getString(offset + 1));
        entity.setTitle(cursor.getString(offset + 2));
        entity.setDes(cursor.getString(offset + 3));
        entity.setBtyp(cursor.getInt(offset + 4));
        entity.setNum(cursor.getInt(offset + 5));
        entity.setTimstr(cursor.getString(offset + 6));
        entity.setTim(cursor.getLong(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Active entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Active entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Active entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
