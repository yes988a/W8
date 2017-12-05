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
 * DAO for table "GROUP".
*/
public class GroupDao extends AbstractDao<Group, Long> {

    public static final String TABLENAME = "GROUP";

    /**
     * Properties of entity Group.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Gid = new Property(1, String.class, "gid", false, "GID");
        public final static Property Gname = new Property(2, String.class, "gname", false, "GNAME");
        public final static Property Gremark = new Property(3, String.class, "gremark", false, "GREMARK");
        public final static Property Nickname = new Property(4, String.class, "nickname", false, "NICKNAME");
        public final static Property Gotice = new Property(5, String.class, "gotice", false, "GOTICE");
        public final static Property Masterid = new Property(6, String.class, "masterid", false, "MASTERID");
        public final static Property Shie = new Property(7, int.class, "shie", false, "SHIE");
        public final static Property Usernum = new Property(8, Integer.class, "usernum", false, "USERNUM");
    }


    public GroupDao(DaoConfig config) {
        super(config);
    }
    
    public GroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"GID\" TEXT NOT NULL ," + // 1: gid
                "\"GNAME\" TEXT NOT NULL ," + // 2: gname
                "\"GREMARK\" TEXT NOT NULL ," + // 3: gremark
                "\"NICKNAME\" TEXT NOT NULL ," + // 4: nickname
                "\"GOTICE\" TEXT NOT NULL ," + // 5: gotice
                "\"MASTERID\" TEXT NOT NULL ," + // 6: masterid
                "\"SHIE\" INTEGER NOT NULL ," + // 7: shie
                "\"USERNUM\" INTEGER NOT NULL );"); // 8: usernum
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_GROUP_GID ON \"GROUP\"" +
                " (\"GID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUP\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Group entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getGid());
        stmt.bindString(3, entity.getGname());
        stmt.bindString(4, entity.getGremark());
        stmt.bindString(5, entity.getNickname());
        stmt.bindString(6, entity.getGotice());
        stmt.bindString(7, entity.getMasterid());
        stmt.bindLong(8, entity.getShie());
        stmt.bindLong(9, entity.getUsernum());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Group entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindString(2, entity.getGid());
        stmt.bindString(3, entity.getGname());
        stmt.bindString(4, entity.getGremark());
        stmt.bindString(5, entity.getNickname());
        stmt.bindString(6, entity.getGotice());
        stmt.bindString(7, entity.getMasterid());
        stmt.bindLong(8, entity.getShie());
        stmt.bindLong(9, entity.getUsernum());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Group readEntity(Cursor cursor, int offset) {
        Group entity = new Group( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getString(offset + 1), // gid
            cursor.getString(offset + 2), // gname
            cursor.getString(offset + 3), // gremark
            cursor.getString(offset + 4), // nickname
            cursor.getString(offset + 5), // gotice
            cursor.getString(offset + 6), // masterid
            cursor.getInt(offset + 7), // shie
            cursor.getInt(offset + 8) // usernum
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Group entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGid(cursor.getString(offset + 1));
        entity.setGname(cursor.getString(offset + 2));
        entity.setGremark(cursor.getString(offset + 3));
        entity.setNickname(cursor.getString(offset + 4));
        entity.setGotice(cursor.getString(offset + 5));
        entity.setMasterid(cursor.getString(offset + 6));
        entity.setShie(cursor.getInt(offset + 7));
        entity.setUsernum(cursor.getInt(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Group entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Group entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Group entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
