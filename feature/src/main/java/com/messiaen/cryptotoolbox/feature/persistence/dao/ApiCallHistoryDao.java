package com.messiaen.cryptotoolbox.feature.persistence.dao;

import com.messiaen.cryptotoolbox.feature.persistence.ApiCallHistory;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ApiCallHistoryDao {

    @Query("SELECT * FROM api_call_history WHERE id = :id")
    ApiCallHistory findById(String id);

    @Insert
    void insert(ApiCallHistory apiCallHistory);

    @Update
    void update(ApiCallHistory apiCallHistory);
}
