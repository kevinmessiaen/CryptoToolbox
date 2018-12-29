package com.messiaen.cryptotoolbox.feature.services;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.data.CoinUpdater;
import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;
import com.messiaen.cryptotoolbox.feature.data.models.Coin;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinUpdatedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.OnNewCoinIdEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnLogoListedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnLogoModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.logo.OnNewLogoEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnNewPriceEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPriceModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.coins.prices.OnPricesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCoinsRequestedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoinsManager {

    @NonNull
    public static final Comparator<Coin> FAVORITES_FIRST = (o1, o2) ->
            -Boolean.compare(o1.isFavorite(), o2.isFavorite());

    @NonNull
    private static final CoinsManager INSTANCE = new CoinsManager();

    @NonNull
    private final Map<String, Coin> map = new HashMap<>();
    @NonNull
    private final List<Coin> coins = new ArrayList<>();

    @Nullable
    private Comparator<Coin> comparator;

    private CoinsManager() {
        EventBus.getDefault().register(this);
    }

    @NonNull
    public static CoinsManager getInstance() {
        return INSTANCE;
    }

    public void requestData() {
        if (coins.isEmpty())
            EventBus.getDefault().post(new OnCoinsRequestedEvent());
        else
            EventBus.getDefault().post(new OnCoinsListModifiedEvent(coins));
    }

    @Nullable
    public Coin requestData(@NonNull String id) {
        Coin request = new Coin(new CoinId(id));
        synchronized (coins) {
            int index = coins.indexOf(request);
            if (index >= 0)
                return coins.get(index);
        }

        EventBus.getDefault().post(new OnCoinsRequestedEvent());
        return null;
    }

    public void setComparator(@Nullable Comparator<Coin> comparator) {
        this.comparator = comparator;

        if (comparator != null) {
            Collections.sort(coins, comparator);

            EventBus.getDefault().post(new OnCoinsListModifiedEvent(coins));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCoinModifiedEvent(@NonNull OnCoinUpdatedEvent event) {
        if (comparator != null) {
            Collections.sort(coins, comparator);
            EventBus.getDefault().post(new OnCoinsListModifiedEvent(coins));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCoinsListed(@NonNull OnCoinsListedEvent event) {
        if (coins.size() > 0 && OnCoinsListedEvent.Source.DATABASE.equals(event.getSource()))
            return;

        List<CoinId> inserted;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            inserted = insert(event.getCoins().stream());
        else
            inserted = insert(event.getCoins());

        if (!inserted.isEmpty()) {
            if (OnCoinsListedEvent.Source.COINGECKO.equals(event.getSource()))
                EventBus.getDefault().post(new OnNewCoinIdEvent(inserted));

            EventBus.getDefault().post(new OnCoinsListModifiedEvent(coins));
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    public List<CoinId> insert(@NonNull Stream<CoinId> ids) {
        List<CoinId> inserted;
        synchronized (coins) {
            inserted = ids.filter(id -> map.get(id.getId()) == null)
                    .collect(Collectors.toList());

            inserted.stream().map(Coin::new).forEach(coin -> {
                coins.add(coin);
                map.put(coin.getId(), coin);
            });

            if (comparator != null)
                coins.sort(comparator);
        }
        return inserted;
    }

    @NonNull
    public List<CoinId> insert(@NonNull List<CoinId> ids) {
        List<CoinId> inserted = new ArrayList<>();
        synchronized (coins) {
            for (CoinId id : ids) {
                if (map.get(id.getId()) == null) {
                    Coin coin = new Coin(id);
                    inserted.add(0, id);
                    coins.add(0, coin);
                    map.put(coin.getId(), coin);
                }
            }

            if (comparator != null)
                Collections.sort(coins, comparator);
        }
        return inserted;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onPricesListed(@NonNull OnPricesListedEvent event) {
        List<Coin> modified;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            modified = modified(event.getPrices().stream());
        else
            modified = modified(event.getPrices());

        if (OnPricesListedEvent.Source.COINGECKO.equals(event.getSource()))
            EventBus.getDefault().post(new OnNewPriceEvent(event.getPrices()));

        EventBus.getDefault().post(new OnPriceModifiedEvent(modified));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onLogoListed(OnLogoListedEvent event) {
        List<Coin> modified;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            modified = modified(event.getLogos().stream());
        else
            modified = modified(event.getLogos());

        if (OnLogoListedEvent.Source.COINGECKO.equals(event.getSource()))
            EventBus.getDefault().post(new OnNewLogoEvent(event.getLogos()));

        EventBus.getDefault().post(new OnLogoModifiedEvent(modified));
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    public List<Coin> modified(@NonNull Stream<? extends CoinUpdater> ids) {
        return ids.map(updated -> {
            Coin c = map.get(updated.getId());
            if (c != null)
                updated.update(c);
            return c;
        }).distinct().collect(Collectors.toList());
    }

    @NonNull
    public List<Coin> modified(@NonNull List<? extends CoinUpdater> ids) {
        List<Coin> modified = new ArrayList<>();

        for (CoinUpdater updater : ids) {
            Coin coin = map.get(updater.getId());
            if (coin != null) {
                updater.update(coin);
                if (!modified.contains(coin))
                    modified.add(0, coin);
            }
        }

        return modified;
    }
}
