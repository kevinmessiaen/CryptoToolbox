package com.messiaen.cryptotoolbox.feature.manager;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.SparseArray;

import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.InvalidateCryptocurrenciesEvent;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolderUpdater;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesDataChangedEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesRequestMapEvent;
import com.messiaen.cryptotoolbox.feature.events.cryptocurrencies.CryptocurrenciesUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.persistence.DatabaseManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private Comparator<CryptocurrencyHolder> comparator;

    private CryptocurrenciesManager() {
        EventBus.getDefault().register(this);
    }

    public static CryptocurrenciesManager getInstance() {
        return INSTANCE;
    }

    public void requestData() {
        if (cryptocurrencies.isEmpty())
            EventBus.getDefault().post(new CryptocurrenciesRequestMapEvent());
        else
            EventBus.getDefault().post(new CryptocurrenciesDataChangedEvent(null, cryptocurrencies));
    }

    public void setComparator(Comparator<CryptocurrencyHolder> comparator) {
        this.comparator = comparator;

        if (comparator != null) {
            Collections.sort(cryptocurrencies, comparator);

            EventBus.getDefault().post(new CryptocurrenciesDataChangedEvent(null, cryptocurrencies));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onUpdate(@NonNull CryptocurrenciesUpdatedEvent event) {
        Collection<? extends CryptocurrencyHolderUpdater> updaters = event.getModified();

        if (updaters == null)
            return;

        List<CryptocurrencyHolder> modified;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            modified = update(updaters.stream());
        else
            modified = update(updaters);

        EventBus.getDefault().post(new CryptocurrenciesDataChangedEvent(modified, cryptocurrencies));
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    private List<CryptocurrencyHolder> update(
            @NonNull Stream<? extends CryptocurrencyHolderUpdater> updaterStream) {
        List<CryptocurrencyHolder> modified;

        synchronized (cryptocurrencies) {
            if (holderMap == null)
                holderMap = new SparseArray<>();

            modified = updaterStream.map(updater -> updater.update(holderMap.get(updater.getId())))
                    .collect(Collectors.toList());

            modified.stream().filter(holder -> holderMap.get(holder.getId()) == null)
                    .forEach(holder -> {
                        cryptocurrencies.add(0, holder);
                        holderMap.put(holder.getId(), holder);
                    });

            if (comparator != null)
                cryptocurrencies.sort(comparator);
        }

        return modified;
    }

    @NonNull
    private List<CryptocurrencyHolder> update(
            @NonNull Collection<? extends CryptocurrencyHolderUpdater> updaters) {
        List<CryptocurrencyHolder> modified;

        synchronized (cryptocurrencies) {
            if (holderMap == null)
                holderMap = new SparseArray<>();

            modified = new ArrayList<>();
            for (CryptocurrencyHolderUpdater updater : updaters) {
                CryptocurrencyHolder holder = updater.update(holderMap.get(updater.getId()));
                if (holderMap.get(holder.getId()) == null) {
                    cryptocurrencies.add(0, holder);
                    holderMap.put(holder.getId(), holder);
                }
                modified.add(holder);
            }

            if (comparator != null)
                Collections.sort(cryptocurrencies, comparator);
        }

        return modified;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void notifyChange(@NonNull CryptocurrenciesDataChangedEvent event) {
        if (event.getModified() == null)
            return;

        synchronized (event.getModified()) {
            DatabaseManager.getDatabase().cryptocurrencyHolderDao().insertAll(event.getModified());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void invalidateData(@NonNull InvalidateCryptocurrenciesEvent event) {
        for (CryptocurrencyHolder holder: cryptocurrencies) {
            holder.setLastTimeInfoRefreshed(null);
            holder.setLastTimePriceRefreshed(null);
        }

        EventBus.getDefault().post(new CryptocurrenciesRequestMapEvent(true));
    }
}
