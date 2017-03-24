package com.bigapps.brooklyn.recordy.dataBase.models;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@DatabaseTable(tableName = Record.TABLE_NAME_RECORDS)
public class Record {

    public static final String TABLE_NAME_RECORDS = "records";

    @DatabaseField(generatedId = true) private long mId;
    @DatabaseField(foreign = true, foreignAutoRefresh = true) private Client mClientId;
    @DatabaseField() private long mDate;
    @DatabaseField(foreign = true, foreignAutoRefresh = true) private Procedure mProcedureId;
    @DatabaseField() private int mDuration;


    public Record() {
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

    public Procedure getmProcedureId() {
        return mProcedureId;
    }

    public void setmProcedureId(Procedure mProcedureId) {
        this.mProcedureId = mProcedureId;
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
