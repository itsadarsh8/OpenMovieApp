package com.example.popularmoviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {


    private int flag = 1;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<MovieDetails> movieDetailsArrayList;
    RecyclerView recyclerView;
    IamAsyncTask asyncTask;
    IamAsyncTask asyncTask2;
    ProgressBar progressbar;
    TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyTextView = findViewById(R.id.emptyTextView);

        movieDetailsArrayList = new ArrayList<MovieDetails>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //Initializing adapter with empty arrayList
        recyclerViewAdapter = new RecyclerViewAdapter(this, movieDetailsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo NI = CM.getActiveNetworkInfo();
        if (NI != null && NI.isConnected()) {
            emptyTextView.setVisibility(GONE);
            asyncTask = new IamAsyncTask();
            asyncTask.execute("https://api.themoviedb.org/3/movie/popular?api_key=8f067240d8717f510b4c79abe9f714b7");


        } else {
            progressbar = findViewById(R.id.progress_bar);
            if (progressbar.getVisibility() == View.VISIBLE) {
                progressbar.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NI = CM.getActiveNetworkInfo();

        if (NI != null && NI.isConnected()) {
            emptyTextView.setVisibility(View.INVISIBLE);

            if (itemThatWasClickedId == R.id.popular) {

                asyncTask = new IamAsyncTask();
                asyncTask.execute("https://api.themoviedb.org/3/movie/popular?api_key=8f067240d8717f510b4c79abe9f714b7");


            }
            if (itemThatWasClickedId == R.id.top_rated) {

                asyncTask2 = new IamAsyncTask();
                asyncTask2.execute("https://api.themoviedb.org/3/movie/top_rated?api_key=8f067240d8717f510b4c79abe9f714b7");

            }


        } else {


            recyclerView.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);

        }


        return true;
    }

    private class IamAsyncTask extends AsyncTask<String, Void, List<MovieDetails>> {

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.INVISIBLE);
            progressbar = findViewById(R.id.progress_bar);
            if (progressbar.getVisibility() == View.GONE) {
                progressbar.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected List<MovieDetails> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            return MovieUtils.fetchMovieDetails(urls[0]);
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movieDetailsList) {
            super.onPostExecute(movieDetailsList);
            progressbar.setVisibility(View.GONE);


            recyclerViewAdapter=new RecyclerViewAdapter(MainActivity.this,movieDetailsList);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            recyclerView.setAdapter(recyclerViewAdapter);


            recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, movieDetailsList);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);


        }
    }
}
