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
 * DAO for table "CHAT".
*/
public class ChatDao extends AbstractDao<Chat, Long> {

    public static final String TABLENAME = "CHAT";

    /**
     * Properties of entity Chat.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Requid = new Property(1, String.class, "requid", false, "REQUID");
        public final static Property Resuid = new Property(2, String.class, "resuid", false, "RESUID");
        public final static Property Tim = new Property(3, long.class, "tim", false, "TIM");
        public final static Property Typ = new Property(4, int.class, "typ", false, "TYP");
        public final static Property Txt = new Property(5, String.class, "txt", false, "TXT");
        public final static Property Stat = new Property(6, int.class, "stat", false, "STAT");
    }


    public ChatDao(DaoConfig config) {
        super(config);
    }
    
    public ChatDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"REQUID\" TEXT NOT NULL ," + // 1: requid
                "\"RESUID\" TEXT NOT NULL ," + // 2: resuid
                "\"TIM\" INTEGER NOT NULL ," + // 3: tim
                "\"TYP\" INTEGER NOT NULL ," + // 4: typ
                "\"TXT\" TEXT NOT NULL ," + // 5: txt
                "\"STAT\" INTEGER NOT NULL );"); // 6: stat
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Chat entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getRequid());
        stmt.bindString(3, entity.getResuid());
        stmt.bindLong(4, entity.getTim());
        stmt.bindLong(5, entity.getTyp());
        stmt.bindString(6, entity.getTxt());
        stmt.bindLong(7, entity.getStat());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Chat entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getRequid());
        stmt.bindString(3, entity.getResuid());
        stmt.bindLong(4, entity.getTim());
        stmt.bindLong(5, entity.getTyp());
        stmt.bindString(6, entity.getTxt());
        stmt.bindLong(7, entity.getStat());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Chat readEntity(Cursor cursor, int offset) {
        Chat entity = new Chat( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getString(offset + 1), // requid
            cursor.getString(offset + 2), // resuid
            cursor.getLong(offset + 3), // tim
            cursor.getInt(offset + 4), // typ
            cursor.getString(offset + 5), // txt
            cursor.getInt(offset + 6) // stat
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Chat entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRequid(cursor.getString(offset + 1));
        entity.setResuid(cursor.getString(offset + 2));
        entity.setTim(cursor.getLong(offset + 3));
        entity.setTyp(cursor.getInt(offset + 4));
        entity.setTxt(cursor.getString(offset + 5));
        entity.setStat(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Chat entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Chat entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Chat entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
