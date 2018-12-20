package com.messiaen.cryptotoolbox.feature.persistence;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "api_call_history")
public class ApiCallHistory {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "last_call")
    private Date lastCall;

    public ApiCallHistory() {

    }

    @Ignore
    public ApiCallHistory(String id, Date lastCall) {
        this.id = id;
        this.lastCall = lastCall;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getLastCall() {
        return lastCall;
    }

    public void setLastCall(Date lastCall) {
        this.lastCall = lastCall;
    }

    public boolean shouldUpdate(long delay) {
        return lastCall == null || new Date(new Date().getTime() - delay).after(lastCall);
    }
}
