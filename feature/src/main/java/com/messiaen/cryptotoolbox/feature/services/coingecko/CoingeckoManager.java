package com.messiaen.cryptotoolbox.feature.services.coingecko;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;
import com.messiaen.cryptotoolbox.feature.data.entities.Logo;
import com.messiaen.cryptotoolbox.feature.data.entities.Price;
import com.messiaen.cryptotoolbox.feature.data.models.Currency;
import com.messiaen.cryptotoolbox.feature.data.results.CoingeckoPingResult;
import com.messiaen.cryptotoolbox.feature.events.coingecko.OnCoingeckoStatusEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnLogoListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPricesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnCurrenciesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.error.OnNetworkErrorEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinLogoRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoingeckoStatusRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinsRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCurrenciesRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnPricesRequestedEvent;
import com.messiaen.cryptotoolbox.feature.services.CurrenciesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoingeckoManager {

    private static final String TAG = "CoingeckoManager";

    private static CoingeckoManager INSTANCE;

    private Map<String, Boolean> lock = new HashMap<>();
    private CoingeckoPingService pinger;
    private CoingeckoCoinsService coins;
    private CoingeckoPriceService prices;

    private CoingeckoManager(@NonNull String apiUrl) {
        EventBus.getDefault().register(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                                .create()))
                .build();

        pinger = retrofit.create(CoingeckoPingService.class);
        coins = retrofit.create(CoingeckoCoinsService.class);
        prices = retrofit.create(CoingeckoPriceService.class);
        Log.d(TAG, "Service initialized at address " + apiUrl);
    }

    public static void init(@NonNull String apiUrl) {
        INSTANCE = new CoingeckoManager(apiUrl);
        new Thread(() ->
                INSTANCE.onRequestPing(new OnCoingeckoStatusRequestedEvent())).start();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onStatus(OnCoingeckoStatusEvent event) {
        if (event.getStatus())
            return;

        if (!lock(pinger.getClass()))
            return;

        if (event.getRetry() > 0)
            try {
                Thread.sleep(event.getRetry());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        onRequestPing(new OnCoingeckoStatusRequestedEvent());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRequestPing(@NonNull OnCoingeckoStatusRequestedEvent event) {
        if (!lock(pinger.getClass()))
            return;

        boolean result = executePing();
        unlock(pinger.getClass());
        EventBus.getDefault().post(
                new OnCoingeckoStatusEvent(result));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRequestCoinList(OnCoinsRequestedEvent event) {
        if (!lock(coins.getClass()))
            return;

        List<CoinId> list = executeCoinList();
        unlock(coins.getClass());

        if (list != null)
            EventBus.getDefault().post(
                    new OnCoinsListedEvent(list, OnCoinsListedEvent.Source.COINGECKO));

        EventBus.getDefault().post(new OnCoingeckoStatusEvent(list != null));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRequestCurrencyList(OnCurrenciesRequestedEvent event) {
        if (!lock(prices.getClass()))
            return;

        List<Currency> list = executeCurrencyList();
        unlock(prices.getClass());

        if (list != null)
            EventBus.getDefault().post(
                    new OnCurrenciesListedEvent(list, OnCurrenciesListedEvent.Source.COINGECKO));

        EventBus.getDefault().post(new OnCoingeckoStatusEvent(list != null));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onRequestPrice(OnPricesRequestedEvent event) {
        if (!lock(prices.getClass()))
            return;

        StringBuilder ids = new StringBuilder();
        Iterator<String> it = event.getCoins().iterator();
        while (it.hasNext()) {
            ids.append(it.next());
            if (it.hasNext())
                ids.append(",");
        }

        List<Price> p = executeGetPrices(ids.toString(),
                CurrenciesManager.getInstance().getLocal().getName());
        unlock(prices.getClass());

        if (p != null)
            EventBus.getDefault().post(new OnPricesListedEvent(p, OnPricesListedEvent.Source.COINGECKO));

        EventBus.getDefault().post(new OnCoingeckoStatusEvent(p != null));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoRequested(@NonNull OnCoinLogoRequestedEvent event) {
        String key = coins.getClass().getName() + "_" + event.getCoin().getCoinId();
        if (!lock(key))
            return;


        Logo logo = executeGetLogo(event.getCoin().getId());
        unlock(key);

        if (logo != null)
            EventBus.getDefault().post(new OnLogoListedEvent(logo, OnLogoListedEvent.Source.COINGECKO));

        EventBus.getDefault().post(new OnCoingeckoStatusEvent(logo != null));
    }

    private boolean lock(Class<?> clazz) {
        return lock(clazz.getName());
    }

    private void unlock(Class<?> clazz) {
        unlock(clazz.getName());
    }

    private boolean lock(String key) {
        Boolean locked = lock.get(key);

        if (locked != null && locked)
            return false;

        lock.put(key, true);
        return true;
    }

    private void unlock(String key) {
        lock.put(key, false);
    }

    private boolean executePing() {
        try {
            Response<CoingeckoPingResult> response = pinger.ping().execute();

            if (response.isSuccessful() && response.body() != null) {
                Log.d(TAG, response.body().getGeckoSays());
                return true;
            } else if (response.errorBody() != null) {
                EventBus.getDefault().post(new OnNetworkErrorEvent(
                        response.code(), response.errorBody().toString(), this.getClass()));
                Log.d(TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new OnNetworkErrorEvent(
                    -1, "Error pinging Coingecko", this.getClass()));
            Log.d(TAG, "Error pinging Coingecko", e);
        }
        return false;
    }

    @Nullable
    private List<CoinId> executeCoinList() {
        try {
            Response<List<CoinId>> response = coins.list().execute();

            if (response.isSuccessful()) {
                return response.body();
            } else if (response.errorBody() != null) {
                EventBus.getDefault().post(new OnNetworkErrorEvent(
                        response.code(), response.errorBody().toString(), this.getClass()));
                Log.d(TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new OnNetworkErrorEvent(
                    -1, "Error listing coins", this.getClass()));
            Log.d(TAG, "Error listing coins", e);
        }
        return null;
    }


    @Nullable
    private List<Currency> executeCurrencyList() {
        try {
            Response<List<String>> response = prices.list().execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    List<Currency> currencies = new ArrayList<>();
                    for (String currency : response.body())
                        currencies.add(new Currency(currency));
                    return currencies;
                }
            } else if (response.errorBody() != null) {
                EventBus.getDefault().post(new OnNetworkErrorEvent(
                        response.code(), response.errorBody().toString(), this.getClass()));
                Log.d(TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new OnNetworkErrorEvent(
                    -1, "Error resolving currencies", this.getClass()));
            Log.d(TAG, "Error resolving currencies", e);
        }
        return null;
    }


    @Nullable
    private List<Price> executeGetPrices(String ids, String currencies) {
        try {
            Response<HashMap<String, JsonObject>> response =
                    prices.getPrices(ids, currencies, true, true).execute();

            if (response.isSuccessful()) {
                HashMap<String, JsonObject> prices = response.body();
                if (prices != null) {
                    List<Price> result = new ArrayList<>();
                    for (String crypto : prices.keySet()) {
                        JsonObject object = prices.get(crypto);
                        if (object != null) {
                            for (String currency : object.keySet()) {
                                if (currency.length() == 3) {
                                    Price price = new Price();
                                    price.setCoinId(crypto);
                                    price.setCurrency(currency);
                                    price.setPrice(object.get(currency).getAsDouble());
                                    if (object.get(currency + "_24h_vol") != null)
                                        price.setVol(object.get(currency + "_24h_vol").getAsDouble());
                                    if (object.get("last_updated_at") != null)
                                        price.setLastUpdatedAt(
                                                new Date(object.get("last_updated_at").getAsLong()));
                                    result.add(price);
                                }
                            }
                        }
                    }
                    return result;
                }
            } else if (response.errorBody() != null) {
                EventBus.getDefault().post(new OnNetworkErrorEvent(
                        response.code(), response.errorBody().toString(), this.getClass()));
                Log.d(TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new OnNetworkErrorEvent(
                    -1, "Error resolving prices", this.getClass()));
            Log.d(TAG, "Error resolving prices", e);
        }
        return null;
    }

    @Nullable
    private Logo executeGetLogo(String id) {
        try {
            Response<JsonObject> response =
                    coins.getMetadata(id,
                            false,
                            false,
                            false,
                            false,
                            false,
                            false).execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    JsonObject image = response.body().getAsJsonObject("image");

                    if (image != null) {
                        Logo logo = new Logo();
                        logo.setId(id);
                        logo.setThumb(image.get("thumb").getAsString());
                        logo.setSmall(image.get("small").getAsString());
                        logo.setLarge(image.get("large").getAsString());
                        return logo;
                    }
                }
            } else if (response.errorBody() != null) {
                EventBus.getDefault().post(new OnNetworkErrorEvent(
                        response.code(), response.errorBody().toString(), this.getClass()));
                Log.d(TAG, response.errorBody().string());
            }
        } catch (IOException e) {
            EventBus.getDefault().post(new OnNetworkErrorEvent(
                    -1, "Error resolving logo", this.getClass()));
            Log.d(TAG, "Error resolving logo", e);
        }
        return null;
    }
}

