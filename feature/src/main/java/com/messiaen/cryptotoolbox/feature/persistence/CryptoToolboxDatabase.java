package com.messiaen.cryptotoolbox.feature.persistence;

import com.messiaen.cryptotoolbox.feature.data.entities.CoinId;
import com.messiaen.cryptotoolbox.feature.data.entities.Logo;
import com.messiaen.cryptotoolbox.feature.data.entities.Price;
import com.messiaen.cryptotoolbox.feature.data.models.Currency;
import com.messiaen.cryptotoolbox.feature.persistence.dao.CoinsDao;
import com.messiaen.cryptotoolbox.feature.persistence.dao.CurrenciesDao;
import com.messiaen.cryptotoolbox.feature.persistence.dao.LogosDao;
import com.messiaen.cryptotoolbox.feature.persistence.dao.PricesDao;
import com.messiaen.cryptotoolbox.feature.utils.Converters;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CoinId.class, Currency.class, Price.class, Logo.class},
        version = 8, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class CryptoToolboxDatabase extends RoomDatabase {

    public abstract CoinsDao coins();

    public abstract CurrenciesDao currencies();

    public abstract PricesDao prices();


    public abstract LogosDao logos();
}
