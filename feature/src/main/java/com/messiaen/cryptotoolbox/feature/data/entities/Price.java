package com.messiaen.cryptotoolbox.feature.data.entities;

import com.messiaen.cryptotoolbox.feature.data.CoinUpdater;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = {"coin_id", "currency"})
public class Price implements CoinUpdater {

    @ColumnInfo(name = "coin_id")
    @NonNull
    private String coinId = "";

    @ColumnInfo
    @NonNull
    private String currency = "";

    @ColumnInfo
    private double price;

    @ColumnInfo(name = "24h_vol")
    private double vol;

    @ColumnInfo(name = "last_updated_at")
    private Date lastUpdatedAt;

    public Price() {
    }

    @Ignore
    public Price(@NonNull String coinId, String currency) {
        this.coinId = coinId;
        this.currency = currency.toLowerCase();
    }

    @NonNull
    @Override
    public String getId() {
        return coinId;
    }

    @NonNull
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull String coinId) {
        this.coinId = coinId;
    }

    public String getCurrency() {
        return currency.toLowerCase();
    }

    public void setCurrency(String currency) {
        this.currency = currency.toLowerCase();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return coinId.equals(price.coinId) &&
                currency.equals(price.currency);
    }

    @Override
    public void update(@NonNull Coin coin) {
        coin.setPrice(this);
    }

    @Override
    @NonNull
    public String toString() {
        return "Price{" +
                "coinId='" + coinId + '\'' +
                ", currency='" + currency + '\'' +
                ", price=" + price +
                ", vol=" + vol +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
