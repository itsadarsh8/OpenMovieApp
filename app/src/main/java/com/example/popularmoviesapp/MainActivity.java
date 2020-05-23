package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesapp.Adapters.FavouriteAdapter;
import com.example.popularmoviesapp.Adapters.MovieDetailRecyclerViewAdapter;
import com.example.popularmoviesapp.ArchitectureComponents.FavouritesViewModel;
import com.example.popularmoviesapp.Objects.FavouriteDetails;
import com.example.popularmoviesapp.Objects.MovieDetails;
import com.example.popularmoviesapp.Utility.MovieUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    MovieDetailRecyclerViewAdapter mMovieDetailRecyclerViewAdapter;
    ArrayList<MovieDetails> movieDetailsArrayList;
    RecyclerView recyclerView;
    IamAsyncTask asyncTask;
    IamAsyncTask asyncTask2;
    ProgressBar progressbar;
    TextView emptyTextView;
    FavouriteAdapter favouriteAdapter;

    private FavouritesViewModel favouritesViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyTextView = findViewById(R.id.emptyTextView);
        recyclerView = findViewById(R.id.recycler_view);
        favouritesViewModel = new FavouritesViewModel(this.getApplication());
        favouriteAdapter = new FavouriteAdapter();

        updateInitiallyWithPopularView();


    }

    //Layout will be updated to popular list for the first  time when acitivity is launched
    private void updateInitiallyWithPopularView() {
        movieDetailsArrayList = new ArrayList<MovieDetails>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMovieDetailRecyclerViewAdapter = new MovieDetailRecyclerViewAdapter(this, movieDetailsArrayList);
        recyclerView.setAdapter(mMovieDetailRecyclerViewAdapter);


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

            } else if (itemThatWasClickedId == R.id.top_rated) {
                asyncTask2 = new IamAsyncTask();
                asyncTask2.execute("https://api.themoviedb.org/3/movie/top_rated?api_key=8f067240d8717f510b4c79abe9f714b7");
            }

        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);

        }
        if (itemThatWasClickedId == R.id.favourite) {

            emptyTextView.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);

            updateFavouriteItems();


            deleteItemOnSwipe();


        }

        return true;
    }

    private void deleteItemOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                favouritesViewModel.delete(favouriteAdapter.getItemAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "removed from favourites", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void updateFavouriteItems() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favouriteAdapter);

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);
        favouritesViewModel.getAllFavourites().observe(this, new Observer<List<FavouriteDetails>>() {
            @Override
            public void onChanged(List<FavouriteDetails> favouriteDetails) {
                favouriteAdapter.setList(favouriteDetails);
            }
        });
        recyclerView.setVisibility(View.VISIBLE);
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


            mMovieDetailRecyclerViewAdapter = new MovieDetailRecyclerViewAdapter(MainActivity.this, movieDetailsList);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(mMovieDetailRecyclerViewAdapter);


            mMovieDetailRecyclerViewAdapter = new MovieDetailRecyclerViewAdapter(MainActivity.this, movieDetailsList);
            //  recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setAdapter(mMovieDetailRecyclerViewAdapter);
            recyclerView.setVisibility(View.VISIBLE);


        }
    }
}
