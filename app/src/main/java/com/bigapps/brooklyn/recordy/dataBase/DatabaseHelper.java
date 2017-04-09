package com.bigapps.brooklyn.recordy.dataBase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bigapps.brooklyn.recordy.dataBase.models.Client;
import com.bigapps.brooklyn.recordy.dataBase.models.Procedure;
import com.bigapps.brooklyn.recordy.dataBase.models.ProceduresRecordList;
import com.bigapps.brooklyn.recordy.dataBase.models.Record;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Brooklyn on 18-Mar-17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "RecordyDataBase1.db";
    private static final int DATABASE_VERSION = 27;

    private Dao<Record, Integer> mRecordDao = null;
    private Dao<Procedure, Integer> mProcedureDao = null;
    private Dao<Client, Integer> mClientDao = null;
    private Dao<ProceduresRecordList, Integer> mProceduresRecordListDao = null;

    private static DatabaseHelper helper = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Record.class);
            TableUtils.createTable(connectionSource, Procedure.class);
            TableUtils.createTable(connectionSource, Client.class);
            TableUtils.createTable(connectionSource, ProceduresRecordList.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Record.class, true);
            TableUtils.dropTable(connectionSource, Procedure.class, true);
            TableUtils.dropTable(connectionSource, Client.class, true);
            TableUtils.dropTable(connectionSource, ProceduresRecordList.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Record, Integer> getRecordDao() throws SQLException {
        if (mRecordDao == null) {
            mRecordDao = getDao(Record.class);
        }
        return mRecordDao;
    }

    public Dao<ProceduresRecordList, Integer> getProceduresRecordListDao() throws SQLException {
        if (mProceduresRecordListDao == null) {
            mProceduresRecordListDao = getDao(ProceduresRecordList.class);
        }
        return mProceduresRecordListDao;
    }

    public Dao<Procedure, Integer> getProcedureDao() throws SQLException {
        if (mProcedureDao == null) {
            mProcedureDao = getDao(Procedure.class);
        }
        return mProcedureDao;
    }

    public Dao<Client, Integer> getClientDao() throws SQLException {
        if (mClientDao == null) {
            mClientDao = getDao(Client.class);
        }
        return mClientDao;
    }

    public List<Procedure> getProcedureByName(String name)  throws SQLException{
        List<Procedure> goalList =
                getProcedureDao().queryBuilder().where().eq("mProcedureName", name).query();
        return goalList;
    }

    @Override
    public void close() {
        mRecordDao = null;
        mProcedureDao = null;
        mClientDao = null;
        super.close();
    }
}