package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolderUpdater;
import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.Platform;

import java.util.Date;

public class CryptocurrencyIDMapDTO implements CryptocurrencyHolderUpdater {

    private int id;
    private String name;
    private String symbol;
    private String slug;
    private int isActive;
    private Date firstHistoricalData;
    private Date lastHistoricalData;
    private PlatformDTO platform;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Date getFirstHistoricalData() {
        return firstHistoricalData;
    }

    public void setFirstHistoricalData(Date firstHistoricalData) {
        this.firstHistoricalData = firstHistoricalData;
    }

    public Date getLastHistoricalData() {
        return lastHistoricalData;
    }

    public void setLastHistoricalData(Date lastHistoricalData) {
        this.lastHistoricalData = lastHistoricalData;
    }

    public PlatformDTO getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDTO platform) {
        this.platform = platform;
    }

    @Override
    public CryptocurrencyHolder update(CryptocurrencyHolder holder) {
        holder = (holder == null) ? new CryptocurrencyHolder() : holder;
        holder.setId(id);
        holder.setName(name);
        holder.setSymbol(symbol);
        holder.setSlug(slug);
        holder.setIsActive(isActive);
        holder.setFirstHistoricalData(firstHistoricalData);
        holder.setLastHistoricalData(lastHistoricalData);
        if (platform != null)
            holder.setPlatform(new Platform(platform));
        return holder;
    }
}
