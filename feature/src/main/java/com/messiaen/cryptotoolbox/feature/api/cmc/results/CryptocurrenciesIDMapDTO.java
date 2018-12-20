package com.messiaen.cryptotoolbox.feature.api.cmc.results;

import com.messiaen.cryptotoolbox.feature.api.cmc.dto.CryptocurrencyIDMapDTO;
import com.messiaen.cryptotoolbox.feature.api.cmc.dto.StatusDTO;

import java.util.List;

public class CryptocurrenciesIDMapDTO {

    private List<CryptocurrencyIDMapDTO> data;

    private StatusDTO status;

    public CryptocurrenciesIDMapDTO() {
    }

    public List<CryptocurrencyIDMapDTO> getData() {
        return data;
    }

    public void setData(List<CryptocurrencyIDMapDTO> data) {
        this.data = data;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

}
