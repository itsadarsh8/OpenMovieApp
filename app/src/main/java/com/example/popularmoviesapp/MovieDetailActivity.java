package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesapp.Adapters.MovieReviewAdapter;
import com.example.popularmoviesapp.Adapters.MovieVideoAdapter;
import com.example.popularmoviesapp.ArchitectureComponents.FavouritesViewModel;
import com.example.popularmoviesapp.Objects.FavouriteDetails;
import com.example.popularmoviesapp.Objects.MovieReview;
import com.example.popularmoviesapp.Utility.MovieUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    MovieVideoAsyncTask asyncTaskVideo;
    MovieReviewAsyncTask asyncTaskReview;
    String mid, title, overView, date, rating;
    RecyclerView videoRecyclerView;
    MovieVideoAdapter movieVideoAdapter;
    RecyclerView reviewRecyclerView;
    MovieReviewAdapter movieReviewAdapter;
    ImageView reviewLoadImage;
    ImageView videoLoadImage;
    FavouritesViewModel favouritesViewModel;
    FavouriteDetails favouriteDetails;
    int flag = 0;
    List<FavouriteDetails> allFavouriteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        updateDetails(intent);
        updateVideo();
        updateReviews();

        reviewLoadImage = findViewById(R.id.review_load_image);
        videoLoadImage = findViewById(R.id.trailer_load_image);
        reviewLoadImage.setVisibility(View.VISIBLE);
        videoLoadImage.setVisibility(View.VISIBLE);
        //  FloatingActionButton floatingActionButton=findViewById(R.id.)

        Picasso.get().load(R.drawable.load5).into(reviewLoadImage);
        Picasso.get().load(R.drawable.load5).into(videoLoadImage);

        asyncTaskVideo = new MovieVideoAsyncTask();
        asyncTaskVideo.execute(getVideoApiLink(mid));

        asyncTaskReview = new MovieReviewAsyncTask();
        asyncTaskReview.execute(getReviewApiLink(mid));


        FloatingActionButton favButton = findViewById(R.id.set_favourite);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < allFavouriteList.size(); i++) {
                    if (allFavouriteList.get(i).getId() == Integer.parseInt(mid)) {
                        flag = 1;
                    }
                }

                if (flag == 0) {

                    addToFavourite();
                    flag = 1;
                } else {
                    flag = 0;
                    deleteFromFavourite();
                }

            }
        });

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouritesViewModel.getAllFavourites().observe(this, new Observer<List<FavouriteDetails>>() {
            @Override
            public void onChanged(List<FavouriteDetails> favouriteDetails) {
                allFavouriteList = favouriteDetails;


            }
        });

    }

    private void deleteFromFavourite() {
        favouriteDetails = new FavouriteDetails(Integer.parseInt(mid), title, overView, date, rating);
        favouritesViewModel.delete(favouriteDetails);
        Toast.makeText(MovieDetailActivity.this, "Deleted from favourite", Toast.LENGTH_SHORT).show();

    }

    private void addToFavourite() {


        favouriteDetails = new FavouriteDetails(Integer.parseInt(mid), title, overView, date, rating);
        favouritesViewModel.insert(favouriteDetails);
        Toast.makeText(MovieDetailActivity.this, "Added to favourite", Toast.LENGTH_SHORT).show();

    }

    //Initiate Reviews View with empty ArrayList
    private void updateReviews() {
        ArrayList<MovieReview> movieReviewArrayList = new ArrayList<>();
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieReviewAdapter = new MovieReviewAdapter(movieReviewArrayList, MovieDetailActivity.this);
        reviewRecyclerView.setAdapter(movieVideoAdapter);
    }

    //Initiate Video View with empty ArrayList
    private void updateVideo() {
        ArrayList<String> movieVideoArrayList = new ArrayList<String>();
        videoRecyclerView = findViewById(R.id.trailer_recycler_view);
        videoRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        movieVideoAdapter = new MovieVideoAdapter(movieVideoArrayList, MovieDetailActivity.this);
        videoRecyclerView.setAdapter(movieVideoAdapter);
    }

    //Updates the details from MovieDetail Intent passed
    private void updateDetails(Intent intent) {
        title = intent.getStringExtra("title");
        overView = intent.getStringExtra("overview");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");
        String image = intent.getStringExtra("image");
        String id = intent.getStringExtra("id");
        mid = id;

        Log.i("MovieDetailActivity-output", title);
        Log.i("MovieDetailActivity-output", overView);
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
        overViewTextView.setText(overView);

        Picasso.get().load(image).placeholder(R.drawable.load5).into(imageView);

    }

    //Creates VideoApi link from id
    private String getVideoApiLink(String id) {
        String videoLink = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=8f067240d8717f510b4c79abe9f714b7&language=en-US";
        return videoLink;
    }

    //Creates ReviewApi link from id
    private String getReviewApiLink(String id) {
        String reviewLink = "https://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=8f067240d8717f510b4c79abe9f714b7&language=en-US";
        return reviewLink;
    }


    public class MovieVideoAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (videoRecyclerView.getVisibility() == View.VISIBLE) {
                videoRecyclerView.setVisibility(View.GONE);
            }
            videoLoadImage.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            return MovieUtils.fetchMovieVideo(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);

            movieVideoAdapter = new MovieVideoAdapter(list, MovieDetailActivity.this);
            videoRecyclerView.setAdapter(movieVideoAdapter);

            if (videoRecyclerView.getVisibility() == View.GONE) {
                videoRecyclerView.setVisibility(View.VISIBLE);
            }
            videoLoadImage.setVisibility(View.GONE);

        }
    }

    public class MovieReviewAsyncTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (reviewRecyclerView.getVisibility() == View.VISIBLE) {
                reviewRecyclerView.setVisibility(View.GONE);
            }
            reviewLoadImage.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MovieReview> doInBackground(String... strings) {

            return MovieUtils.fetchMovieReview(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieReview> movieReviews) {
            super.onPostExecute(movieReviews);

            movieReviewAdapter = new MovieReviewAdapter(movieReviews, MovieDetailActivity.this);
            reviewRecyclerView.setAdapter(movieReviewAdapter);

            if (reviewRecyclerView.getVisibility() == View.GONE) {
                reviewRecyclerView.setVisibility(View.VISIBLE);
                reviewLoadImage.setVisibility(View.GONE);

            }
        }
    }
}
