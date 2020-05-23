package com.example.popularmoviesapp.Objects;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_table")
public class FavouriteDetails {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mRating;

    public FavouriteDetails(String title, String description, String date, String rating) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mRating = rating;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDate() {
        return mDate;
    }

    public String getRating() {
        return mRating;
    }
}
