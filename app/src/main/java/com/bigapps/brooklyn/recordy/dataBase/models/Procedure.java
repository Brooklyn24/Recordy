package com.bigapps.brooklyn.recordy.dataBase.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Brooklyn on 18-Mar-17.
 */

@DatabaseTable(tableName = Procedure.TABLE_NAME_PROCEDURE)
public class Procedure {

    public static final String TABLE_NAME_PROCEDURE = "procedures";

    @DatabaseField(generatedId = true) private int mId;
    @DatabaseField private String mProcedureName;
    @DatabaseField private int mPrice;
    @DatabaseField private int mDuration;

    public Procedure() {
    }

    public Procedure(String mProcedureName, int mPrice, int mDuration) {
        this.mProcedureName = mProcedureName;
        this.mPrice = mPrice;
        this.mDuration = mDuration;
    }

    public String getmProcedureName() {
        return mProcedureName;
    }

    public int getmId() {
        return mId;
    }
    public int getmPrice() {
        return mPrice;
    }

    public int getmDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return mProcedureName + mId;
    }
}
