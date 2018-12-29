package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.data.entities.Price;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PricesDao {

    @Query("select * from price")
    List<Price> list();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Price> prices);

}
