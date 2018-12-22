package com.messiaen.cryptotoolbox.feature.api.cmc.results;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyQuotesDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.StatusDTO;

import java.util.Map;

public class CryptocurrenciesQuotesDTO {

    private Map<String, CryptocurrencyQuotesDTO> data;

    private StatusDTO status;

    public CryptocurrenciesQuotesDTO() {
    }

    public Map<String, CryptocurrencyQuotesDTO> getData() {
        return data;
    }

    public void setData(Map<String, CryptocurrencyQuotesDTO> data) {
        this.data = data;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }
}
