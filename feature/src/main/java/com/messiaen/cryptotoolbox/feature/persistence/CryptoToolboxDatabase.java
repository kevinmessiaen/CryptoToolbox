package com.messiaen.cryptotoolbox.feature.persistence;

import com.messiaen.cryptotoolbox.feature.data.cryptocurrency.CryptocurrencyHolder;
import com.messiaen.cryptotoolbox.feature.persistence.dao.ApiCallHistoryDao;
import com.messiaen.cryptotoolbox.feature.persistence.dao.CryptocurrencyHolderDao;
import com.messiaen.cryptotoolbox.feature.utils.Converters;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CryptocurrencyHolder.class, ApiCallHistory.class}, version = 5, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CryptoToolboxDatabase extends RoomDatabase {

    public abstract CryptocurrencyHolderDao cryptocurrencyHolderDao();

    public abstract ApiCallHistoryDao apiCallHistoryDao();

}
