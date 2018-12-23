package com.messiaen.cryptotoolbox.feature.manager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.CryptocurrenciesMapQuery;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.CryptocurrenciesUpdatingQuery;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesOutdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesRequestMapEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import androidx.annotation.NonNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoinMarketCapManager {

    private static String API_NAME = "COIN_MARKET_CAP";

    private static String API_VERSION = "V1";

    private static String BASE_ID = API_NAME + "_" + API_VERSION;

    public static String MAP_ALL_CRYPTOCURRENCIES = CoinMarketCapManager.BASE_ID + "_CRYPTOCURRENCY_MAP";

    private static long ONE_HOUR = 1000 * 60 * 60;
    private static long ONE_DAY = ONE_HOUR * 24;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY_IN_DAYS = 1;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY =
            ONE_DAY * AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY_IN_DAYS;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY_IN_DAYS = 7;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY =
            ONE_DAY * AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY_IN_DAYS;

    private static long AUTO_UPDATE_CRYPTOCURRENCY_LISTING_DELAY_IN_HOURS = 1;
    public static long AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY =
            ONE_HOUR * AUTO_UPDATE_CRYPTOCURRENCY_LISTING_DELAY_IN_HOURS;

    private final static CoinMarketCapManager INSTANCE = new CoinMarketCapManager();

    private Retrofit retrofit;

    private final Map<String, Boolean> requests = new HashMap<>();
    private final Map<String, Object> services = new HashMap<>();

    private CoinMarketCapManager() {
        EventBus.getDefault().register(this);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void refreshCryptocurrencies(CryptocurrenciesOutdatedEvent event) {
        Boolean state = requests.get(event.getDataType().toString());
        if (state != null && !state)
            return;

        requests.put(event.getDataType().toString(), true);
        new CryptocurrenciesUpdatingQuery.Builder().event(event).build().execute();
        requests.put(event.getDataType().toString(), false);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void mapCryptocurrencies(CryptocurrenciesRequestMapEvent event) {
        if (!event.isOnline())
            return;

        Boolean state = requests.get(MAP_ALL_CRYPTOCURRENCIES);
        if (state != null && !state)
            return;

        requests.put(MAP_ALL_CRYPTOCURRENCIES, true);
        new CryptocurrenciesMapQuery().execute();
        requests.put(MAP_ALL_CRYPTOCURRENCIES, false);
    }

    public static<O> O getService(@NonNull Class<O> clazz) {
        return INSTANCE.getServiceLocal(clazz);
    }

    @SuppressWarnings("unchecked")
    private <O> O getServiceLocal(@NonNull Class<O> clazz) {
        if (services.containsKey(clazz.getName()))
            return (O) services.get(clazz.getName());

        O service = retrofit.create(clazz);
        services.put(clazz.getName(), service);

        return service;
    }

    public enum DataType {
        METADATA(100, holder -> holder.shouldUpdateMetadata(AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY)),
        QUOTES(100, holder -> holder.shouldUpdateQuotes(AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY));

        private final int size;
        private final Function<CryptocurrencyHolder, Boolean> filter;

        DataType(int size, Function<CryptocurrencyHolder, Boolean> filter) {
            this.size = size;
            this.filter = filter;
        }

        public int getSize() {
            return size;
        }

        public Function<CryptocurrencyHolder, Boolean> getFilter() {
            return filter;
        }
    }
}
