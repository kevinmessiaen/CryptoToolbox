package com.messiaen.cryptotoolbox.feature.api.cmc;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinMarketCap {

    private static String API_NAME = "COIN_MARKET_CAP";

    private static String API_VERSION = "V1";

    public static String BASE_ID = API_NAME + "_" + API_VERSION;

    public static String MAP_ALL_CRYPTOCURRENCIES = CoinMarketCap.BASE_ID + "_CRYPTOCURRENCY_MAP";

    private static long ONE_HOUR = 1000l * 60l * 60l;
    private static long ONE_DAY = ONE_HOUR * 24l;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY_IN_DAYS = 1l;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY =
            ONE_DAY * AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY_IN_DAYS;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY_IN_DAYS = 7l;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY =
            ONE_DAY * AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY_IN_DAYS;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_LISTING_DELAY_IN_HOURS = 1l;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY =
            ONE_HOUR * AUTO_UPDATE_CRYPTOCURRENCY_LISTING_DELAY_IN_HOURS;

    private final static CoinMarketCap INSTANCE = new CoinMarketCap();

    private Retrofit retrofit;

    private Map<String, Object> services = new HashMap<>();

    private CoinMarketCap() {

    }

    public static void init(@NonNull String apiUrl) {
        INSTANCE.retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                                .create()))
                .build();
    }

    public static <O> O getService(@NonNull Class<O> clazz) {
        return INSTANCE.getServiceIntern(clazz);
    }

    @SuppressWarnings("unchecked")
    private <O> O getServiceIntern(@NonNull Class<O> clazz) {
        if (services.containsKey(clazz.getName()))
            return (O) services.get(clazz.getName());

        O service = retrofit.create(clazz);
        services.put(clazz.getName(), service);

        return service;
    }
}
