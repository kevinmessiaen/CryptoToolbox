package com.messiaen.cryptotoolbox.feature.persistence.entities;

import android.annotation.TargetApi;
import android.os.Build;

import com.messiaen.cryptotoolbox.feature.utils.Currencies;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CryptocurrencyHolder implements CryptocurrencyHolderUpdater {

    @PrimaryKey
    private int id;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String symbol;

    @ColumnInfo
    private String category;

    @ColumnInfo
    private String slug;

    @ColumnInfo
    private String logo;

    @ColumnInfo(name = "is_active")
    private int isActive = 1;

    @ColumnInfo(name = "first_historical_data")
    private Date firstHistoricalData;

    @ColumnInfo(name = "last_historical_data")
    private Date lastHistoricalData;

    @ColumnInfo(name = "cmc_rank")
    private int cmcRank = Integer.MAX_VALUE;

    @ColumnInfo(name = "num_market_pairs")
    private int numMarketPairs;

    @ColumnInfo(name = "circulating_supply")
    private double circulatingSupply;

    @ColumnInfo(name = "total_supply")
    private double totalSupply;

    @ColumnInfo(name = "max_supply")
    private double maxSupply;

    @ColumnInfo(name = "last_updated")
    private Date lastUpdated;

    @ColumnInfo(name = "date_added")
    private Date dateAdded;

    @ColumnInfo
    private List<String> tags;

    @Embedded
    private Platform platform;

    @Embedded
    private Urls urls;

    @ColumnInfo
    private Map<String, Quote> quote;

    @ColumnInfo(name = "last_time_info_refreshed")
    private Date lastTimeInfoRefreshed;

    @ColumnInfo(name = "last_time_price_refreshed")
    private Date lastTimePriceRefreshed;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite = false;

    public CryptocurrencyHolder() {

    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public int getCmcRank() {
        return cmcRank;
    }

    public void setCmcRank(int cmcRank) {
        this.cmcRank = cmcRank;
    }

    public int getNumMarketPairs() {
        return numMarketPairs;
    }

    public void setNumMarketPairs(int numMarketPairs) {
        this.numMarketPairs = numMarketPairs;
    }

    public double getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(double circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public double getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(double maxSupply) {
        this.maxSupply = maxSupply;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Map<String, Quote> getQuote() {
        return quote;
    }

    public void setQuote(Map<String, Quote> quote) {
        this.quote = quote;
    }

    public Date getLastTimeInfoRefreshed() {
        return lastTimeInfoRefreshed;
    }

    public void setLastTimeInfoRefreshed(Date lastTimeInfoRefreshed) {
        this.lastTimeInfoRefreshed = lastTimeInfoRefreshed;
    }

    public Date getLastTimePriceRefreshed() {
        return lastTimePriceRefreshed;
    }

    public void setLastTimePriceRefreshed(Date lastTimePriceRefreshed) {
        this.lastTimePriceRefreshed = lastTimePriceRefreshed;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean shouldUpdateMetadata(long delay) {
        return lastTimeInfoRefreshed == null ||
                new Date(new Date().getTime() - delay).after(lastTimeInfoRefreshed);
    }

    public boolean shouldUpdateQuotes(long delay) {
        return lastTimePriceRefreshed == null || quote == null ||
                new Date(new Date().getTime() - delay).after(lastTimePriceRefreshed) ||
                !quote.containsKey(Currencies.getDefaultLocal().toString());
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean filter(Stream<String> filterStream) {
        if (name == null || symbol == null)
            return false;

        return filterStream
                .map(s -> name.toUpperCase().contains(s) || symbol.toUpperCase().contains(s))
                .reduce(false, (a, b) -> a || b);
    }

    public boolean filter(String... filters) {
        if (name == null || symbol == null)
            return false;

        for (String filter : filters)
            if (name.toUpperCase().contains(filter) || symbol.toUpperCase().contains(filter))
                return true;

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptocurrencyHolder that = (CryptocurrencyHolder) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public CryptocurrencyHolder update(CryptocurrencyHolder holder) {
        return this;
    }
}
