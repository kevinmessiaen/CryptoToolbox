package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CryptocurrencyHolderDao {

    @Query("SELECT * FROM cryptocurrencyholder")
    List<CryptocurrencyHolder> findAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CryptocurrencyHolder> cryptocurrencies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CryptocurrencyHolder... cryptocurrencies);


}
