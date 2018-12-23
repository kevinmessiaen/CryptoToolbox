package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolderUpdater;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Platform;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Quote;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CryptocurrencyQuotesDTO  implements CryptocurrencyHolderUpdater {

    private int id;
    private String name;
    private String symbol;
    private String slug;
    private int cmcRank = Integer.MAX_VALUE;
    private int numMarketPairs;
    private double circulatingSupply;
    private double totalSupply;
    private double maxSupply;
    private Date lastUpdated;
    private Date dateAdded;
    private List<String> tags;
    private PlatformDTO platform;
    private Map<String, QuoteDTO> quote;

    public CryptocurrencyQuotesDTO() {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public PlatformDTO getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDTO platform) {
        this.platform = platform;
    }

    public Map<String, QuoteDTO> getQuote() {
        return quote;
    }

    public void setQuote(Map<String, QuoteDTO> quote) {
        this.quote = quote;
    }

    @Override
    public CryptocurrencyHolder update(CryptocurrencyHolder holder) {
        holder = (holder == null) ? new CryptocurrencyHolder() : holder;
        holder.setId(id);
        holder.setName(name);
        holder.setSymbol(symbol);
        holder.setSlug(slug);
        holder.setCmcRank(cmcRank);
        holder.setNumMarketPairs(numMarketPairs);
        holder.setCirculatingSupply(circulatingSupply);
        holder.setTotalSupply(totalSupply);
        holder.setMaxSupply(maxSupply);
        holder.setLastUpdated(lastUpdated);
        holder.setDateAdded(dateAdded);
        holder.setTags(tags);
        if (platform != null)
            holder.setPlatform(new Platform(platform));
        Map<String, Quote> quote = holder.getQuote();
        if (quote == null)
            quote = new HashMap<>();
        QuoteDTO q;
        for (String key : this.quote.keySet())
            if ((q = this.quote.get(key)) != null)
                quote.put(key, new Quote(q));
        holder.setQuote(quote);
        holder.setLastTimePriceRefreshed(new Date());
        return holder;
    }
}
