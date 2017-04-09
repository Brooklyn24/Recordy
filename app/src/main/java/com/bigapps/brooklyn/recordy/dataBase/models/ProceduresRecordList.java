package com.bigapps.brooklyn.recordy.dataBase.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Brooklyn on 04-Apr-17.
 */
@DatabaseTable(tableName = ProceduresRecordList.TABLE_NAME_PROCEDURE)
public class ProceduresRecordList {

    public static final String TABLE_NAME_PROCEDURE = "proceduresRecordsList";

    @DatabaseField(generatedId = true) private int mId;
    @DatabaseField(foreign = true, foreignAutoRefresh = true) private Record mRecord;
    @DatabaseField private long mProcedureId;

    public ProceduresRecordList() {
    }

    public void setRecord(Record record) {
        mRecord = record;
    }

    public long getmProcedureId() {
        return mProcedureId;
    }

    public void setmProcedureId(long mProcedureId) {
        this.mProcedureId = mProcedureId;
    }
}
