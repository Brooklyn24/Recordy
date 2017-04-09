package com.bigapps.brooklyn.recordy.dataBase.models;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.bigapps.brooklyn.recordy.dataBase.DatabaseHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@DatabaseTable(tableName = Record.TABLE_NAME_RECORDS)
public class Record {

    public static final String TABLE_NAME_RECORDS = "records";

    @DatabaseField(generatedId = true) private long mId;
    @DatabaseField(foreign = true, foreignAutoRefresh = true) private Client mClientId;
    @DatabaseField() private long mDate;
    @ForeignCollectionField(eager = true) private Collection<ProceduresRecordList> proceduresList;
    @DatabaseField() private int mDuration;


    public Record() {
        proceduresList = new ArrayList<>();
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public long getmId() {
        return mId;
    }

    public Client getmClientId() {
        return mClientId;
    }

    public void setmClientId(Client mClientId) {
        this.mClientId = mClientId;
    }

    public long getmDate() {
        return mDate;
    }

    public void setmDate(long mDate) {
        this.mDate = mDate;
    }

    public void addProcedure(Procedure procedure){
        ProceduresRecordList value = new ProceduresRecordList();
        value.setmProcedureId(procedure.getmId());
        value.setRecord(this);
        try {
            DatabaseHelper.getHelper(null).getProceduresRecordListDao().create(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        proceduresList.add(value);
    }

    public void removProcedure(Procedure value){
        try {
            DatabaseHelper.getHelper(null).getProcedureDao().delete(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        proceduresList.remove(value);
    }

    public WeekViewEvent toEvent(int month) {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.setTimeInMillis(mDate);
        if (startTime.get(Calendar.MONTH) != month){
            return null;
        }
        endTime.setTimeInMillis(mDate + mDuration * 60000);
        WeekViewEvent event = new WeekViewEvent(
                mId,
                mClientId.getName(),
                startTime,
                endTime
        );
        return event;
    }
}
