package com.messiaen.cryptotoolbox.feature.api.cmc.queries;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;

import java.util.List;

public class IdsQueryParameters {

    private String id;

    public IdsQueryParameters() {
    }

    public IdsQueryParameters(String id) {
        this.id = id;
    }

    public IdsQueryParameters(List<CryptocurrencyHolder> cryptocurrencies) {
        id = "";

        for (int i = 0; i < cryptocurrencies.size(); i++)
            id += cryptocurrencies.get(i).getId() + ((i + 1 < cryptocurrencies.size()) ? "," : "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
