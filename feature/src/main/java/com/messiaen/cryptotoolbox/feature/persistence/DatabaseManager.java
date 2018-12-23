package com.messiaen.cryptotoolbox.feature.persistence;

import android.content.Context;

import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesRequestMapEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.manager.CoinMarketCapManager;
import com.messiaen.cryptotoolbox.feature.persistence.entities.ApiCallHistory;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Room;

public class DatabaseManager {

    private final static DatabaseManager INSTANCE = new DatabaseManager();

    private CryptoToolboxDatabase cryptoToolboxDatabase;

    private DatabaseManager() {
        EventBus.getDefault().register(this);
    }

    public static void init(@NonNull Context context, @NonNull String databaseName) {
        new Thread(new DatabaseBuilder(context, databaseName)).start();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void mapCryptocurrencies(CryptocurrenciesRequestMapEvent event) {
        if (event.isOnline())
            return;

        List<CryptocurrencyHolder> cryptocurrencies =
                cryptoToolboxDatabase.cryptocurrencyHolderDao().findAll();

        if (cryptocurrencies != null && cryptocurrencies.size() > 0) {
            ApiCallHistory history =
                    cryptoToolboxDatabase.apiCallHistoryDao()
                            .findById(CoinMarketCapManager.MAP_ALL_CRYPTOCURRENCIES);

            if (history == null || history.shouldUpdate(
                    CoinMarketCapManager.AUTO_UPDATE_CRYPTOCURRENCY_MAP_DELAY))
                EventBus.getDefault().post(new CryptocurrenciesRequestMapEvent(true));

            EventBus.getDefault().post(new CryptocurrenciesUpdatedEvent(cryptocurrencies));
        }

        EventBus.getDefault().post(new CryptocurrenciesRequestMapEvent(true));
    }

    public static CryptoToolboxDatabase getDatabase() {
        return INSTANCE.cryptoToolboxDatabase;
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
            INSTANCE.cryptoToolboxDatabase =
                    Room.databaseBuilder(context, CryptoToolboxDatabase.class, databaseName)
                            .fallbackToDestructiveMigration()
                            .build();
        }
    }
}
