package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.entities.CryptocurrencyHolderUpdater;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Platform;
import com.messiaen.cryptotoolbox.feature.persistence.entities.Urls;

import java.util.Date;
import java.util.List;

public class CryptocurrencyMetadataDTO implements CryptocurrencyHolderUpdater {

    private int id;
    private String name;
    private String symbol;
    private String category;
    private String slug;
    private String logo;
    private List<String> tags;
    private PlatformDTO platform;
    private UrlsDTO urls;

    public CryptocurrencyMetadataDTO() {
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

    public UrlsDTO getUrls() {
        return urls;
    }

    public void setUrls(UrlsDTO urls) {
        this.urls = urls;
    }

    @Override
    public CryptocurrencyHolder update(CryptocurrencyHolder holder) {
        holder = (holder == null) ? new CryptocurrencyHolder() : holder;
        holder.setId(id);
        holder.setName(name);
        holder.setSymbol(symbol);
        holder.setCategory(category);
        holder.setSlug(slug);
        holder.setLogo(logo);
        holder.setTags(tags);
        if (platform != null)
            holder.setPlatform(new Platform(platform));
        if (urls != null)
            holder.setUrls(new Urls(urls));
        holder.setLastTimeInfoRefreshed(new Date());
        return holder;
    }
}
