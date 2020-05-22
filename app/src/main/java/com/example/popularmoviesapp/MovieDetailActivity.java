package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmoviesapp.Objects.MovieReview;
import com.example.popularmoviesapp.Utility.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    MovieVideoAsyncTask asyncTaskVideo;
    MovieReviewAsyncTask asyncTaskReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String overview = intent.getStringExtra("overview");
        String date = intent.getStringExtra("date");
        String rating = intent.getStringExtra("rating");
        String image = intent.getStringExtra("image");
        String id = intent.getStringExtra("id");

        Log.i("MovieDetailActivity-output", title);
        Log.i("MovieDetailActivity-output", overview);
        Log.i("MovieDetailActivity-output", date);
        Log.i("MovieDetailActivity-output", rating);
        Log.i("MovieDetailActivity-output", image);
        Log.i("MovieDetailActivity-output", id);

        TextView titleTextView = findViewById(R.id.title);
        TextView dateTextView = findViewById(R.id.date);
        TextView ratingTextView = findViewById(R.id.rating);
        TextView overViewTextView = findViewById(R.id.overView);
        ImageView imageView = findViewById(R.id.image);
        titleTextView.setText(title);
        dateTextView.setText("Release Date:" + date);
        ratingTextView.setText("Rating: " + rating);
        overViewTextView.setText(overview);

        Picasso.get().load(image).placeholder(R.drawable.load3).into(imageView);


        asyncTaskVideo = new MovieVideoAsyncTask();
        asyncTaskVideo.execute(getVideoApiLink(id));

        asyncTaskReview = new MovieReviewAsyncTask();
        asyncTaskReview.execute(getReviewApiLink(id));


    }

    //Creates VideoApi link from id
    private String getVideoApiLink(String id) {
        String videoLink = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=8f067240d8717f510b4c79abe9f714b7&language=en-US";
        return videoLink;
    }

    private String getReviewApiLink(String id) {
        String reviewLink = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=8f067240d8717f510b4c79abe9f714b7&language=en-US&page=1";
        return reviewLink;
    }


    public class MovieVideoAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            return MovieUtils.fetchMovieVideo(strings[0]);
        }
    }

    public class MovieReviewAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            MovieUtils.fetchMovieReview(strings[0]);
            return null;
        }
    }
}
