package com.messiaen.cryptotoolbox.feature.services;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.data.models.Currency;
import com.messiaen.cryptotoolbox.feature.events.coins.OnCoinsListedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnCurrenciesListModifiedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnCurrenciesListedEvent;
import com.messiaen.cryptotoolbox.feature.events.currencies.OnNewCurrencyEvent;
import com.messiaen.cryptotoolbox.feature.events.requests.OnCurrenciesRequestedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public class CurrenciesManager {

    private static final CurrenciesManager INSTANCE = new CurrenciesManager();

    private Currency local;
    private final List<Currency> currencies = new ArrayList<>();

    private CurrenciesManager() {
        EventBus.getDefault().register(this);
    }

    public static CurrenciesManager getInstance() {
        return INSTANCE;
    }

    public void requestData() {
        if (currencies.isEmpty())
            EventBus.getDefault().post(new OnCurrenciesRequestedEvent());
        else
            EventBus.getDefault().post(new OnCurrenciesListModifiedEvent(local, currencies));
    }

    @NonNull
    public Currency getLocal() {
        return local == null ? new Currency("usd") : local;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onCurrencyListed(@NonNull OnCurrenciesListedEvent event) {
        if (currencies.size() > 0 && OnCurrenciesListedEvent.Source.DATABASE.equals(event.getSource()))
            return;

        List<Currency> inserted;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            inserted = insert(event.getCurrencies().stream());
        else
            inserted = insert(event.getCurrencies());

        if (!inserted.isEmpty()) {
            if (OnCoinsListedEvent.Source.COINGECKO.equals(event.getSource()))
                EventBus.getDefault().post(new OnNewCurrencyEvent(inserted));


            Currency local = new Currency(
                    NumberFormat.getCurrencyInstance().getCurrency().getCurrencyCode());

            this.local = currencies.contains(local) ? local : new Currency("usd");

            EventBus.getDefault().post(new OnCurrenciesListModifiedEvent(this.local, currencies));
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @NonNull
    public List<Currency> insert(@NonNull Stream<Currency> currencies) {
        List<Currency> inserted;
        synchronized (this.currencies) {
            inserted = currencies.filter(currency -> !this.currencies.contains(currency))
                    .collect(Collectors.toList());

            this.currencies.addAll(inserted);
        }
        return inserted;
    }

    @NonNull
    public List<Currency> insert(@NonNull List<Currency> currencies) {
        List<Currency> inserted = new ArrayList<>();
        synchronized (this.currencies) {
            for (Currency currency : currencies) {
                if (!this.currencies.contains(currency)) {
                    inserted.add(currency);
                    this.currencies.add(currency);
                }
            }
        }
        return inserted;
    }

}
