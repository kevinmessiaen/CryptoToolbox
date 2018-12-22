package com.messiaen.cryptotoolbox.feature.utils;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class Formatters {

    private final static Map<Currencies, NumberFormat> currencies = new HashMap<>();

    private Formatters() {
    }

    public static NumberFormat getCurrencyFormat(String currencyString) {
        Currencies currencyEnum;
        try {
            currencyEnum = Currencies.valueOf(currencyString.toUpperCase());
        } catch (IllegalArgumentException e) {
            currencyEnum = Currencies.getDefault();
        }

        if (currencies.get(currencyEnum) != null)
            return currencies.get(currencyEnum);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(3);
        Currency currency = Currency.getInstance(currencyEnum.toString());
        currencyFormat.setCurrency(currency);
        currencies.put(currencyEnum, currencyFormat);

        return currencyFormat;
    }


}
