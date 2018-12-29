package com.messiaen.cryptotoolbox.feature.data.models;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;
import com.messiaen.cryptotoolbox.feature.data.entities.Logo;
import com.messiaen.cryptotoolbox.feature.data.entities.Price;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public class Coin {

    @NonNull
    private CoinId coinId;

    private Map<String, Price> prices = new HashMap<>();

    private Logo logo;

    private boolean showMenu = false;

    public Coin(@NonNull CoinId coinId) {
        this.coinId = coinId;
    }

    @NonNull
    public CoinId getCoinId() {
        return coinId;
    }

    public void setCoinId(@NonNull CoinId coinId) {
        this.coinId = coinId;
    }

    public Map<String, Price> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Price> prices) {
        this.prices = prices;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public Price getPrice(String currency) {
        return prices.get(currency);
    }

    public void setPrice(Price price) {
        prices.put(price.getCurrency(), price);
    }

    public String getId() {
        return coinId.getId();
    }

    public String getName() {
        return coinId.getName();
    }

    public String getSymbol() {
        return coinId.getSymbol();
    }

    public String getLogoThumb() {
        return (logo == null) ? null : logo.getThumb();
    }

    public String getLogoSmall() {
        return (logo == null) ? null : logo.getSmall();
    }

    public String getLogoLarge() {
        return (logo == null) ? null : logo.getLarge();
    }

    public boolean isFavorite() {
        return coinId.getIsFavorite();
    }

    public void setFavorite(boolean isFavorite) {
        coinId.setIsFavorite(isFavorite);
    }

    public boolean isShowMenu() {
        return showMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean filter(Stream<String> filterStream) {
        if (getName() == null || getSymbol() == null)
            return false;

        return filterStream
                .map(s -> getName().toUpperCase().contains(s) ||
                        getSymbol().toUpperCase().contains(s))
                .reduce(false, (a, b) -> a || b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return coinId.equals(coin.coinId);
    }

    public boolean filter(String... filters) {
        if (getName() == null || getSymbol() == null)
            return false;

        for (String filter : filters)
            if (getName().toUpperCase().contains(filter) ||
                    getSymbol().toUpperCase().contains(filter))
                return true;

        return false;
    }

    public boolean shouldUpdatePrice(String currency) {
        Price price = getPrice(currency);
        return price == null ||
                price.getLastUpdatedAt().before(new Date(new Date().getTime() - 60 * 1000));
    }

    public void setCoin(Coin coin) {
        coinId = coin.coinId;
        prices = coin.prices;
        logo = coin.logo;
        showMenu = coin.showMenu;
    }
}
