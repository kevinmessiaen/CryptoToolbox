package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CoinsDao {

    @Query("select * from coinid")
    List<CoinId> list();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<CoinId> ids);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(CoinId id);

}
