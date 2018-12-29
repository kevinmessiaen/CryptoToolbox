package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.data.models.Currency;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CurrenciesDao {

    @Query("select * from currency")
    List<Currency> list();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Currency> currencies);

}
