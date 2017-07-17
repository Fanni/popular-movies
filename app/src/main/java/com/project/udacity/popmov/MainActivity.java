package com.project.udacity.popmov;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {

    private GridView movieGridView;
    private ArrayList<Movie> movieArrayList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieGridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        if (savedInstanceState == null) {
            getMoviesData(getMovieSortMethod());
        } else {
            if(savedInstanceState.containsKey(getString(R.string.movie_arraylist_key))) {
                movieArrayList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_arraylist_key));
                movieGridView.setAdapter(new MovieAdapter(this, movieArrayList));
            }
            else
                movieGridView.setAdapter(new MovieAdapter(this, new ArrayList<Movie>()));
        }

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), MainDetailActivity.class);
                intent.putExtra(getResources().getString(R.string.data_for_detail_activity), movie);

                startActivity(intent);
            }
        });
    }

    // checking if network is available and device already connected
    private boolean isDeviceConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void getMoviesData(String parameter) {
        if (isDeviceConnected()) {
            TaskCallback taskCallback = new TaskCallback() {
                @Override
                public void callBackAfterTaskFinished(List<Movie> movies) {
                    if (movies.size() == 0) {
                        movies = new ArrayList<>();
                    }
                    movieGridView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    progressBar.setVisibility(View.INVISIBLE);
                }
            };

            progressBar.setVisibility(View.VISIBLE);
            FetchMoviesAsyncTask fetchMoviesAsyncTask = new FetchMoviesAsyncTask(taskCallback);
            fetchMoviesAsyncTask.execute(parameter);
        } else {
            Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int numMovieObjects = movieGridView.getCount();
        if (numMovieObjects > 0) {
            Movie[] movies = new Movie[numMovieObjects];
            for (int i = 0; i < numMovieObjects; i++) {
                movies[i] = (Movie) movieGridView.getItemAtPosition(i);
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
                getMoviesData(getMovieSortMethod());
                return true;
            case R.id.popular:
                this.setTitle(R.string.app_name);
                updateSharedData(getString(R.string.key_movie_shared_popularity));
                getMoviesData(getMovieSortMethod());
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
}
