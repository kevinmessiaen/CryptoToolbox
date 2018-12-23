package com.messiaen.cryptotoolbox.feature.persistence.entities;

public interface CryptocurrencyHolderUpdater {

    int getId();
    
    CryptocurrencyHolder update(CryptocurrencyHolder holder);

}
