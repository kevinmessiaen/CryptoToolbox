package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import java.util.Date;

public class CryptocurrencyIDMapDTO {

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
}
