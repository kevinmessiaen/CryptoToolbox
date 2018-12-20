package com.messiaen.cryptotoolbox.feature.api.cmc.results;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.StatusDTO;

import java.util.List;

public class CryptocurrenciesListingDTO {

    private List<CryptocurrencyQuotesDTO> data;

    private StatusDTO status;

    public CryptocurrenciesListingDTO() {
    }

    public List<CryptocurrencyQuotesDTO> getData() {
        return data;
    }

    public void setData(List<CryptocurrencyQuotesDTO> data) {
        this.data = data;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }
}
