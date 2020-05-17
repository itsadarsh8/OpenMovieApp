package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

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

        Log.i("MovieDetailActivity-output", title);
        Log.i("MovieDetailActivity-output", overview);
        Log.i("MovieDetailActivity-output", date);
        Log.i("MovieDetailActivity-output", rating);
        Log.i("MovieDetailActivity-output", image);

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


    }
}
