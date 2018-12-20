package com.messiaen.cryptotoolbox.feature.api.cmc.results;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyMetadataDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.StatusDTO;

import java.util.Map;

public class CryptocurrenciesMetadataDTO {

    private Map<String, CryptocurrencyMetadataDTO> data;

    private StatusDTO status;

    public CryptocurrenciesMetadataDTO() {
    }

    public Map<String, CryptocurrencyMetadataDTO> getData() {
        return data;
    }

    public void setData(Map<String, CryptocurrencyMetadataDTO> data) {
        this.data = data;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

}
