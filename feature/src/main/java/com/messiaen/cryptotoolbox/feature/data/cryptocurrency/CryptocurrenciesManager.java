package com.messiaen.cryptotoolbox.feature.data.cryptocurrency;

import android.util.SparseArray;

import com.messiaen.cryptotoolbox.feature.api.cmc.CoinMarketCap;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyMetadataDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.queries.IdsQueryParameters;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.CryptocurrenciesQuotesOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.GatherCryptocurrenciesMetadataOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.GetCryptocurrenciesIdMapOnCoinMarketCap;
import com.messiaen.cryptotoolbox.feature.tasks.cryptocurrencies.ListCryptocurrenciesOnDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class CryptocurrenciesManager {

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

    private boolean updatingData = false;

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
            Collections.sort(this.cryptocurrencies, comparator);

            for (OnCryptocurrenciesDataEventListener listener : listeners)
                listener.onCryptocurrenciesChanged(cryptocurrencies);
        }
    }

    private void findAll() {
        new ListCryptocurrenciesOnDatabase().execute();
    }

    public void refreshMetadata(int pos, List<CryptocurrencyHolder> holders) {
        if (updatingData)
            return;

        updatingData = true;

        List<CryptocurrencyHolder> requestedMetadata = new ArrayList<>();

        int i = pos;
        do {
            if (holders.get(i)
                    .shouldUpdateMetadata(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_METADATA_DELAY))
                requestedMetadata.add(holders.get(i));
        } while (requestedMetadata.size() < 100 && ++i < holders.size());

        IdsQueryParameters query = new IdsQueryParameters(requestedMetadata);

        new GatherCryptocurrenciesMetadataOnCoinMarketCap(query).execute();
    }

    public void refreshQuotes(int pos, List<CryptocurrencyHolder> holders) {
        if (updatingData)
            return;

        updatingData = true;

        List<CryptocurrencyHolder> requestedQuotes = new ArrayList<>();

        synchronized (cryptocurrencies) {
            int i = pos;
            do {
                if (holders.get(i)
                        .shouldUpdateQuotes(CoinMarketCap.AUTO_UPDATE_CRYPTOCURRENCY_QUOTES_DELAY))
                    requestedQuotes.add(holders.get(i));
            } while (requestedQuotes.size() < 100 && ++i < holders.size());
        }

        IdsQueryParameters query = new IdsQueryParameters(requestedQuotes);

        new CryptocurrenciesQuotesOnCoinMarketCap(query)
                .execute();
    }

    public void updateIdMap(@NonNull List<CryptocurrencyIDMapDTO> cryptocurrencies) {
        synchronized (this.cryptocurrencies) {
            if (holderMap == null)
                holderMap = new SparseArray<>();

            for (CryptocurrencyIDMapDTO cryptocurrency : cryptocurrencies) {
                if (holderMap.get(cryptocurrency.getId()) != null) {
                    holderMap.get(cryptocurrency.getId()).update(cryptocurrency);
                } else {
                    CryptocurrencyHolder cryptocurrencyHolder = new CryptocurrencyHolder();
                    cryptocurrencyHolder.update(cryptocurrency);
                    this.cryptocurrencies.add(0, cryptocurrencyHolder);
                    holderMap.put(cryptocurrencyHolder.getId(), cryptocurrencyHolder);
                }
            }
        }

        updatingData = false;

        notifyAllChanged();
    }

    public void updateMetadata(@NonNull Map<String, CryptocurrencyMetadataDTO> cryptocurrencies) {
        synchronized (this.cryptocurrencies) {
            for (CryptocurrencyMetadataDTO cryptocurrency : cryptocurrencies.values()) {
                if (holderMap.get(cryptocurrency.getId()) != null) {
                    holderMap.get(cryptocurrency.getId()).update(cryptocurrency);
                } else {
                    CryptocurrencyHolder cryptocurrencyHolder = new CryptocurrencyHolder();
                    cryptocurrencyHolder.update(cryptocurrency);
                    this.cryptocurrencies.add(0, cryptocurrencyHolder);
                    holderMap.put(cryptocurrencyHolder.getId(), cryptocurrencyHolder);
                }
            }
        }

        updatingData = false;

        notifyAllChanged();
    }

    public void updateQuotes(@NonNull Map<String, CryptocurrencyQuotesDTO> cryptocurrencies) {
        synchronized (this.cryptocurrencies) {
            for (CryptocurrencyQuotesDTO cryptocurrency : cryptocurrencies.values()) {
                if (holderMap.get(cryptocurrency.getId()) != null) {
                    holderMap.get(cryptocurrency.getId()).update(cryptocurrency);
                } else {
                    CryptocurrencyHolder cryptocurrencyHolder = new CryptocurrencyHolder();
                    cryptocurrencyHolder.update(cryptocurrency);
                    this.cryptocurrencies.add(0, cryptocurrencyHolder);
                    holderMap.put(cryptocurrencyHolder.getId(), cryptocurrencyHolder);
                }
            }
        }

        updatingData = false;

        notifyAllChanged();
    }

    public void notifyChange(CryptocurrencyHolder holder) {
        synchronized (this.cryptocurrencies) {
            if (comparator != null)
                Collections.sort(this.cryptocurrencies, comparator);

            DatabaseManager.getDatabase().cryptocurrencyHolderDao().insertAll(holder);
        }

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    private void notifyAllChanged() {
        synchronized (cryptocurrencies) {
            if (comparator != null)
                Collections.sort(this.cryptocurrencies, comparator);

            DatabaseManager.getDatabase().cryptocurrencyHolderDao().insertAll(this.cryptocurrencies);
        }

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onCryptocurrenciesChanged(cryptocurrencies);
    }

    public void onListCryptocurrencies(List<CryptocurrencyHolder> cryptocurrencies) {
        synchronized (this.cryptocurrencies) {
            if (cryptocurrencies == null)
                return;

            this.holderMap = new SparseArray<>();

            for (CryptocurrencyHolder cryptocurrency : cryptocurrencies) {
                this.cryptocurrencies.add(0, cryptocurrency);
                holderMap.put(cryptocurrency.getId(), cryptocurrency);
            }

            if (comparator != null)
                Collections.sort(this.cryptocurrencies, comparator);

            updatingData = false;

            for (OnCryptocurrenciesDataEventListener listener : listeners)
                listener.onCryptocurrenciesChanged(this.cryptocurrencies);
        }
    }

    public void onListCryptocurrenciesFailed(int code, @NonNull String message) {
        updatingData = false;

        for (OnCryptocurrenciesDataEventListener listener : listeners)
            listener.onNetworkError(code, message);
    }

    public void onRequestCryptocurrenciesUpdate() {
        if (updatingData)
            return;

        updatingData = true;

        new GetCryptocurrenciesIdMapOnCoinMarketCap().execute();
    }

}
