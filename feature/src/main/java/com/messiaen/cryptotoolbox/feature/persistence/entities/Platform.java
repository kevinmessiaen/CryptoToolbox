package com.messiaen.cryptotoolbox.feature.persistence.entities;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.PlatformDTO;

import androidx.room.ColumnInfo;

public class Platform {

    @ColumnInfo(name = "plaform_id")
    private int id;

    @ColumnInfo(name = "platform_name")
    private String name;

    @ColumnInfo(name = "platform_symbol")
    private String symbol;

    @ColumnInfo(name = "plaform_slug")
    private String slug;

    public Platform() {
    }

    public Platform(PlatformDTO platform) {
        if (platform == null)
            return;

        id = platform.getId();
        name = platform.getName();
        symbol = platform.getSymbol();
        slug = platform.getSlug();
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
}
