package com.messiaen.cryptotoolbox.feature.api.cmc.dto;

import java.util.List;

public class CryptocurrencyMetadataDTO {

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
}
