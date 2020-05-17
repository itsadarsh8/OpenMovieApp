package com.example.popularmoviesapp;

public class MovieDetails {

    private String mMovieTitle;
    private String mMovieRatings;
    private String mMovieImage;
    private String mMovieDate;
    private String mMovieSynopsis;

    public MovieDetails(String movieTitle, String movieDate, String movieRatings, String movieSynopsis, String movieImage) {
        mMovieDate = movieDate;
        mMovieImage = movieImage;
        mMovieRatings = movieRatings;
        mMovieSynopsis = movieSynopsis;
        mMovieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getMovieRatings() {
        return mMovieRatings;
    }

    public String getMovieImage() {
        return mMovieImage;
    }

    public String getMovieDate() {
        return mMovieDate;
    }

    public String getMovieSynopsis() {
        return mMovieSynopsis;
    }


}
