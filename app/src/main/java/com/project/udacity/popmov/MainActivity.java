package com.project.udacity.popmov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener, FetchMoviesAsyncTask.TaskCallback {

    @BindView(R.id.recycler_view) RecyclerView movieRecycleView;
    private ArrayList<Movie> movieArrayList;
    @BindView(R.id.pb_loading) ProgressBar progressBar;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // From stackoverflow --> https://stackoverflow.com/a/38472370/3487252
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 140);
        //

        movieRecycleView.setLayoutManager(new GridLayoutManager(this, noOfColumns));

        if (savedInstanceState == null) {
            setMovieArray(getMovieSortMethod());
        } else {
            if(savedInstanceState.containsKey(getString(R.string.movie_arraylist_key))) {
                movieArrayList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_arraylist_key));
            }
            else {
                movieArrayList = new ArrayList<>();
            }
            movieAdapter = new MovieAdapter(this, movieArrayList);
            movieRecycleView.setAdapter(movieAdapter);
            movieAdapter.setClickListener(this);
        }
    }

    // checking if network is available and device already connected
    private boolean isDeviceConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setMovieArray(String parameter) {
        if (isDeviceConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            FetchMoviesAsyncTask fetchMoviesAsyncTask = new FetchMoviesAsyncTask(MainActivity.this);
            fetchMoviesAsyncTask.execute(parameter);
        } else {
            Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovieObjects = movieAdapter.getItemCount();
        if (numMovieObjects > 0) {
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = movieAdapter.getItem(i);
            }
            ArrayList<Movie> movieArray = new ArrayList<>(Arrays.asList(movies));
            outState.putParcelableArrayList(getString(R.string.movie_arraylist_key), movieArray);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated:
                this.setTitle(R.string.app_name_toprated_movie);
                updateSharedData(getString(R.string.key_movie_shared_toprated));
                setMovieArray(getMovieSortMethod());
                return true;
            case R.id.popular:
                this.setTitle(R.string.app_name);
                updateSharedData(getString(R.string.key_movie_shared_popularity));
                setMovieArray(getMovieSortMethod());
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private String getMovieSortMethod() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        return prefs.getString(getString(R.string.movie_arraylist_key),
                getString(R.string.key_movie_shared_popularity));
    }

    private void updateSharedData(String data) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.movie_arraylist_key),data);
        editor.apply();
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = movieAdapter.getItem(position);

        Intent intent = new Intent(getApplicationContext(), MainDetailActivity.class);
        intent.putExtra(getResources().getString(R.string.data_for_detail_activity), movie);

        startActivity(intent);
    }

    @Override
    public void callBackAfterTaskFinished(ArrayList<Movie> movies) {
        if (movies.size() == 0) {
            movies = new ArrayList<>();
        }
        movieAdapter = new MovieAdapter(this, movies);
        movieRecycleView.setAdapter(movieAdapter);
        movieAdapter.setClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
