package com.example.popularmoviesapp.ArchitectureComponents;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.popularmoviesapp.Objects.FavouriteDetails;

@Database(entities = FavouriteDetails.class, version = 1)
public abstract class FavouriteDatabase extends RoomDatabase {

    private static FavouriteDatabase instance;

    public abstract FavouriteDao favouriteDao();

    // synchronized- Only one thread at a time can access it.
    public static synchronized FavouriteDatabase getInstance(Context context) {
        //It can only be created once(for the first time)
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , FavouriteDatabase.class
                    , "favourite_database")
                    .fallbackToDestructiveMigration()  //To prevent crash while incrementing version.
                    .build();


        }

        return instance;
    }

    public static RoomDatabase.Callback PopulateDb = new RoomDatabase.Callback() {

        //Called when database is created for the first time
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDbAsyncTask(instance).execute();
            super.onCreate(db);
        }
    };


    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavouriteDao favouriteDao;

        public PopulateDbAsyncTask(FavouriteDatabase db) {
            this.favouriteDao = db.favouriteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            favouriteDao.insert(new FavouriteDetails(1,"Title 1", "Discription 1", "2019-12-07", "5.6"));
            favouriteDao.insert(new FavouriteDetails(2,"Title 2", "Discription 2", "2019-12-08", "6.6"));
            favouriteDao.insert(new FavouriteDetails(3,"Title 3", "Discription 3", "2019-12-09", "7.6"));

            return null;
        }
    }
}
