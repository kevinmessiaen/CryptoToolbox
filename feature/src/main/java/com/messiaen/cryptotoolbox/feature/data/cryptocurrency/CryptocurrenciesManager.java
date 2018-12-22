package com.messiaen.cryptotoolbox.feature.data.cryptocurrency;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.SparseArray;

import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.IdsQueryParameters;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.CryptocurrenciesQuotesOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.GatherCryptocurrenciesMetadataOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.GetCryptocurrenciesIdMapOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.ListCryptocurrenciesOnDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public class CryptocurrenciesManager {

    public static final int MAP = 0, METADATA = 1, QUOTES = 2;

    private static final Comparator<CryptocurrencyHolder> ID_ASC =
            (o1, o2) -> Integer.compare(o1.getId(), o2.getId());

    public static final Comparator<CryptocurrencyHolder> FAVORITES_FIRST =
            (o1, o2) -> Boolean.compare(o1.getIsFavorite(), o2.getIsFavorite()) == 0 ?
                    ID_ASC.compare(o1, o2) :
                    -Boolean.compare(o1.getIsFavorite(), o2.getIsFavorite());

    private static final CryptocurrenciesManager INSTANCE = new CryptocurrenciesManager();

    private SparseArray<CryptocurrencyHolder> holderMap;
    private final List<CryptocurrencyHolder> cryptocurrencies = new ArrayList<>();

    private List<OnCryptocurrenciesDataEventListener> listeners = new ArrayList<>();

    private Comparator<CryptocurrencyHolder> comparator;

    @SuppressWarnings("unchecked")
    private AsyncTask<Void, Void, Void>[] tasks = new AsyncTask[3];

    private CryptocurrenciesManager() {

    }

    public static CryptocurrenciesManager getInstance() {
        return INSTANCE;
    }

    public void registerListener(@NonNull OnCryptocurrenciesDataEventListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);

        if (cryptocurrencies.isEmpty())
            findAll();
        else
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    public void unregisterListener(@NonNull OnCryptocurrenciesDataEventListener listener) {
        listeners.remove(listener);
    }

    public void setComparator(Comparator<CryptocurrencyHolder> comparator) {
        this.comparator = comparator;

        if (comparator != null) {
            Collections.sort(cryptocurrencies, comparator);

            for (OnCryptocurrenciesDataEventListener listener : listeners)
                listener.onCryptocurrenciesChanged(cryptocurrencies);
        }
    }

    private void findAll() {
        if (tasks[MAP] != null)
            return;

        tasks[MAP] = new ListCryptocurrenciesOnDatabase();
        tasks[MAP].execute();
    }

    public void refreshMetadata(int pos, List<CryptocurrencyHolder> holders) {
        if (tasks[METADATA] != null)
            return;

        tasks[METADATA] = new GatherCryptocurrenciesMetadataOnCoinMarketCap(
                new IdsQueryParameters.Builder()
                        .holders(holders)
                        .pos(pos)
                        .size(100)
                        .filter(holder -> holder.shouldUpdateMetadata(
                                CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
                        .build());

        tasks[METADATA].execute();
    }

    public void refreshQuotes(int pos, List<CryptocurrencyHolder> holders) {
        if (tasks[QUOTES] != null)
            return;

        tasks[QUOTES] = new CryptocurrenciesQuotesOnCoinMarketCap(
                new IdsQueryParameters.Builder()
                        .holders(holders)
                        .pos(pos)
                        .size(100)
                        .filter(holder -> holder.shouldUpdateQuotes(
                                CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY))
                        .build());

        tasks[QUOTES].execute();
    }

    public void notifyChange(CryptocurrencyHolder... holders) {
        synchronized (this.cryptocurrencies) {
            if (comparator != null)
                Collections.sort(this.cryptocurrencies, comparator);

            DatabaseManager.getDatabase().cryptocurrencyHolderDao().insertAll(holders);
        }

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    private void notifyChange(List<CryptocurrencyHolder> holders) {
        synchronized (this.cryptocurrencies) {
            if (comparator != null)
                Collections.sort(this.cryptocurrencies, comparator);

            DatabaseManager.getDatabase().cryptocurrencyHolderDao().insertAll(holders);
        }

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    public void onUpdate(Collection<? extends CryptocurrencyHolderUpdater> updaters, int type) {
        if (updaters == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            update(updaters.stream());
        else
            update(updaters);

        tasks[type] = null;

        notifyChange(cryptocurrencies);

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    public void onListCryptocurrenciesFailed(int code, @NonNull String message, int type) {
        tasks[type] = null;

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onNetworkError(code, message);
    }

    public void onRequestCryptocurrenciesUpdate() {
        if (tasks[MAP] instanceof GetCryptocurrenciesIdMapOnCoinMarketCap)
            return;

        tasks[MAP] = new GetCryptocurrenciesIdMapOnCoinMarketCap();
        tasks[MAP].execute();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void update(Stream<? extends CryptocurrencyHolderUpdater> updaterStream) {
        synchronized (cryptocurrencies) {
            if (holderMap == null)
                holderMap = new SparseArray<>();

            updaterStream.map(updater -> updater.update(holderMap.get(updater.getId())))
                    .filter(holder -> holderMap.get(holder.getId()) == null)
                    .forEach(holder -> {
                        cryptocurrencies.add(0, holder);
                        holderMap.put(holder.getId(), holder);
                    });
        }
    }

    private void update(Collection<? extends CryptocurrencyHolderUpdater> updaters) {
        synchronized (cryptocurrencies) {
            if (holderMap == null)
                holderMap = new SparseArray<>();

            for (CryptocurrencyHolderUpdater updater : updaters) {
                CryptocurrencyHolder holder = updater.update(holderMap.get(updater.getId()));
                if (holderMap.get(holder.getId()) == null) {
                    cryptocurrencies.add(0, holder);
                    holderMap.put(holder.getId(), holder);
                }
            }
        }
    }

}
