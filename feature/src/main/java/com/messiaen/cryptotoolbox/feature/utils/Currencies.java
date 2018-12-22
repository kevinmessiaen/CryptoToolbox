package com.messiaen.cryptotoolbox.feature.utils;

import java.text.NumberFormat;
import java.util.Currency;

public enum Currencies {

    USD, AED, ALL, AMD, ARS, AUD, AZN, BAM, BDT, BGN, BHD, BMD, BOB, BRL, BYN, CAD,
    CHF, CLP, CNY, COP, CRC, CUP, CZK, DKK, DOP, DZD, EGP, EUR, GBP, GEL, GHS, GTQ,
    HKD, HNL, HRK, HUF, IDR, ILS, INR, IQD, IRR, ISK, JMD, JOD, JPY, KES, KGS, KHR,
    KRW, KWD, KZT, LBP, LKR, MAD, MDL, MKD, MMK, MNT, MUR, MXN, MYR, NAD, NGN, NIO,
    NOK, NPR, NZD, QMR, PAB, PEN, PHP, PKR, PLN, QAR, RON, RSD, RUB, SAR, SEK, SGD,
    SSP, THB, THD, TRY, TTD, TWD, UAH, UGX, UYU, UZS, VES, VND, ZAR;

    public static Currencies getDefaultLocal() {
        try {
            return Currencies.valueOf(
                    NumberFormat.getCurrencyInstance().getCurrency().getCurrencyCode());
        } catch (IllegalArgumentException e) {
            return Currencies.getDefault();
        }
    }

    public static Currencies getDefault() {
        return USD;
    }

}
