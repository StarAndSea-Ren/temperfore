package com.yueh.ren.temforecast.model.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.yueh.ren.temforecast.model.database.Temper;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TEMPER".
*/
public class TemperDao extends AbstractDao<Temper, String> {

    public static final String TABLENAME = "TEMPER";

    /**
     * Properties of entity Temper.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Time = new Property(0, String.class, "time", true, "TIME");
        public final static Property Temperature = new Property(1, double.class, "temperature", false, "TEMPERATURE");
    };


    public TemperDao(DaoConfig config) {
        super(config);
    }
    
    public TemperDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TEMPER\" (" + //
                "\"TIME\" TEXT PRIMARY KEY NOT NULL ," + // 0: time
                "\"TEMPERATURE\" REAL NOT NULL );"); // 1: temperature
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TEMPER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Temper entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getTime());
        stmt.bindDouble(2, entity.getTemperature());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Temper readEntity(Cursor cursor, int offset) {
        Temper entity = new Temper( //
            cursor.getString(offset + 0), // time
            cursor.getDouble(offset + 1) // temperature
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Temper entity, int offset) {
        entity.setTime(cursor.getString(offset + 0));
        entity.setTemperature(cursor.getDouble(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Temper entity, long rowId) {
        return entity.getTime();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Temper entity) {
        if(entity != null) {
            return entity.getTime();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}