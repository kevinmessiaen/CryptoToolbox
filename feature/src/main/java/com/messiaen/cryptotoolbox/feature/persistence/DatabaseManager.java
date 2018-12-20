package com.messiaen.cryptotoolbox.feature.persistence;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

public class DatabaseManager {

    private final static DatabaseManager INSTANCE = new DatabaseManager();

    private CryptoToolboxDatabase cryptoToolboxDatabase;

    private DatabaseManager() {

    }

    public static void init(@NonNull Context context, @NonNull String databaseName) {
        new Thread(new DatabaseBuilder(context, databaseName)).start();
    }

    public static CryptoToolboxDatabase getDatabase() {
        return INSTANCE.cryptoToolboxDatabase;
    }

    private static class DatabaseBuilder implements Runnable {

        private Context context;
        private String databaseName;

        public DatabaseBuilder(@NonNull Context context, @NonNull String databaseName) {
            this.context = context;
            this.databaseName = databaseName;
        }

        @Override
        public void run() {
            INSTANCE.cryptoToolboxDatabase =
                    Room.databaseBuilder(context, CryptoToolboxDatabase.class, databaseName)
                            .fallbackToDestructiveMigration()
                            .build();
        }
    }
}
