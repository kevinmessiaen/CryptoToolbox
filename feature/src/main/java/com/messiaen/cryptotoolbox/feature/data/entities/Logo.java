package com.messiaen.cryptotoolbox.feature.data.entities;

import com.messiaen.cryptotoolbox.feature.data.CoinUpdater;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Logo implements CoinUpdater {

    @PrimaryKey
    @NonNull
    private String id = "";

    @ColumnInfo
    private String thumb;

    @ColumnInfo
    private String small;

    @ColumnInfo
    private String large;

    public Logo() {
    }

    @Ignore
    public Logo(@NonNull String id, String thumb, String small, String large) {
        this.id = id;
        this.thumb = thumb;
        this.small = small;
        this.large = large;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    @Override
    public void update(@NonNull Coin coin) {
        coin.setLogo(this);
    }
}
