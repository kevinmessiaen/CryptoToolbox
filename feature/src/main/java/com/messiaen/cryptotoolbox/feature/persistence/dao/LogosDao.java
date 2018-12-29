package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.data.entities.Logo;
import com.messiaen.cryptotoolbox.feature.data.entities.Price;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LogosDao {

    @Query("select * from logo")
    List<Logo> list();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Logo> logos);

}
