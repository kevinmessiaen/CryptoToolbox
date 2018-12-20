package com.messiaen.cryptotoolbox.feature.data.cryptocurrency;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.QuoteDTO;

import java.util.Date;

public class Quote {

    private double price;
    private double volume_24h;
    private double marketCap;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;
    private Date lastUpdated;

    public Quote() {

    }

    public Quote(QuoteDTO quote) {
        price = quote.getPrice();
        volume_24h = quote.getVolume_24h();
        marketCap = quote.getMarketCap();
        percent_change_1h = quote.getPercent_change_1h();
        percent_change_24h = quote.getPercent_change_24h();
        percent_change_7d = quote.getPercent_change_7d();
        lastUpdated = quote.getLastUpdated();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(double volume_24h) {
        this.volume_24h = volume_24h;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
