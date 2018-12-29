package com.messiaen.cryptotoolbox.feature.data.models;

import java.text.NumberFormat;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Currency {

    @PrimaryKey
    @NonNull
    private String name = "";

    public Currency() {
    }

    @Ignore
    public Currency(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    @NonNull
    public NumberFormat getFormat() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(3);

        try {
            java.util.Currency currency = java.util.Currency.getInstance(name.toUpperCase());
            currencyFormat.setCurrency(currency);
        } catch (IllegalArgumentException e) {
            return NumberFormat.getInstance();
        }

        return currencyFormat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return name.equalsIgnoreCase(currency.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
