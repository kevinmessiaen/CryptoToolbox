package com.messiaen.cryptotoolbox.feature.services;

import android.content.Context;

import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnNewCoinIdEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnLogoListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnNewLogoEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnCurrenciesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnNewCurrencyEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnNewPriceEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPricesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinsRequestedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCurrenciesRequestedEvent;
import com.messiaen.cryptotoolbox.feature.persistence.CryptoToolboxDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.room.Room;

public class DatabaseManager {

    private final static DatabaseManager INSTANCE = new DatabaseManager();

    private CryptoToolboxDatabase database;

    private DatabaseManager() {
        EventBus.getDefault().register(this);
    }

    public static void init(@NonNull Context context, @NonNull String databaseName) {
        new Thread(new DatabaseBuilder(context, databaseName)).start();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void listCoins(OnCoinsRequestedEvent event) {
        if (event.isOnline())
            return;

        EventBus.getDefault().post(
                new OnCoinsListedEvent(database.coins().list(),
                        OnCoinsListedEvent.Source.DATABASE));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void listCurrencied(OnCurrenciesRequestedEvent event) {
        if (event.isOnline())
            return;

        EventBus.getDefault().post(
                new OnCurrenciesListedEvent(database.currencies().list(),
                        OnCurrenciesListedEvent.Source.DATABASE));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCoinListed(OnCoinsListedEvent event) {
        if (!OnCoinsListedEvent.Source.DATABASE.equals(event.getSource()))
            return;

        EventBus.getDefault().post(
                new OnPricesListedEvent(database.prices().list(),
                        OnPricesListedEvent.Source.DATABASE));

        EventBus.getDefault().post(
                new OnLogoListedEvent(database.logos().list(),
                        OnLogoListedEvent.Source.DATABASE));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void insetCoins(OnNewCoinIdEvent event) {
        database.coins().insert(event.getModified());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void insetCoins(OnCoinUpdatedEvent event) {
        database.coins().update(event.getCoin().getCoinId());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void insetCurrencies(OnNewCurrencyEvent event) {
        database.currencies().insert(event.getModified());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void insertPrices(OnNewPriceEvent event) {
        database.prices().insert(event.getValues());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void instertLogo(OnNewLogoEvent event) {
        database.logos().insert(event.getLogos());
    }

    private static class DatabaseBuilder implements Runnable {

        private Context context;
        private String databaseName;

        private DatabaseBuilder(@NonNull Context context, @NonNull String databaseName) {
            this.context = context;
            this.databaseName = databaseName;
        }

        @Override
        public void run() {
            INSTANCE.database =
                    Room.databaseBuilder(context, CryptoToolboxDatabase.class, databaseName)
                            .fallbackToDestructiveMigration()
                            .build();
        }
    }
}
