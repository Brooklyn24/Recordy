package com.bigapps.brooklyn.recordy.dataBase.models;

import android.net.Uri;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Brooklyn on 18-Mar-17.
 */

@DatabaseTable(tableName = Client.TABLE_NAME_CLIENT)
public class Client {

    public static final String TABLE_NAME_CLIENT = "clients";

    @DatabaseField(generatedId = true) private int mId;
    @DatabaseField private String name;
    @DatabaseField private String phone;
    @DatabaseField private String uri;

    public Client() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
