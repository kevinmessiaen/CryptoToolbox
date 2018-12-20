package com.messiaen.cryptotoolbox.feature.data.cryptocurrency;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyMetadataDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CryptocurrencyHolder {

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
        return (lastTimeInfoRefreshed == null) ? true :
                new Date(new Date().getTime() - delay).after(lastTimeInfoRefreshed);
    }

    public boolean shouldUpdateQuotes(long delay) {
        return (lastTimePriceRefreshed == null) ? true :
                new Date(new Date().getTime() - delay).after(lastTimePriceRefreshed);
    }

    public void update(CryptocurrencyIDMapDTO data) {
        id = data.getId();
        name = data.getName();
        symbol = data.getSymbol();
        slug = data.getSlug();
        isActive = data.getIsActive();
        firstHistoricalData = data.getFirstHistoricalData();
        lastHistoricalData = data.getLastHistoricalData();
        platform = new Platform(data.getPlatform());
    }

    public void update(CryptocurrencyMetadataDTO data) {
        id = data.getId();
        category = data.getCategory();
        logo = data.getLogo();
        tags = data.getTags();
        urls = new Urls(data.getUrls());

        lastTimeInfoRefreshed = new Date();
    }

    public void update(CryptocurrencyQuotesDTO data) {
        id = data.getId();
        cmcRank = data.getCmcRank();
        numMarketPairs = data.getNumMarketPairs();
        circulatingSupply = data.getCirculatingSupply();
        totalSupply = data.getTotalSupply();
        maxSupply = data.getMaxSupply();
        lastUpdated = data.getLastUpdated();
        dateAdded = data.getDateAdded();

        if (quote == null)
            quote = new HashMap<>();

        for (String key : data.getQuote().keySet())
            quote.put(key, new Quote(data.getQuote().get(key)));

        lastTimePriceRefreshed = new Date();
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
}
