package com.example.popularmoviesapp.ArchitectureComponents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.popularmoviesapp.Objects.FavouriteDetails;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Insert
    void insert(FavouriteDetails favouriteDetails);

    @Delete
    void delete(FavouriteDetails favouriteDetails);

    @Query("SELECT * FROM favourite_table ORDER BY mId ASC")
    LiveData<List<FavouriteDetails>> getAllFavourite();



}
