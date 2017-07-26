package com.project.udacity.popmov;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ikhwan on 7/9/17.
 */

public class FetchMoviesAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final TaskCallback callbackDelegate;

    FetchMoviesAsyncTask(TaskCallback taskCallback) {
        this.callbackDelegate = taskCallback;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String movieJsonString = null;
        String BASE_URL = "https://api.themoviedb.org/3/movie";
        String API_KEY_PARAM = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(params[0])
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.KEY)
                .build();

        try {
            URL url = new URL(uri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            movieJsonString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            return getMoviesFromJson(movieJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @org.jetbrains.annotations.Contract("null -> null")
    private ArrayList<Movie> getMoviesFromJson(String movieJsonString) throws JSONException {
        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String BACKDROP_IMAGE_PATH = "backdrop_path";

        if (movieJsonString == null || "".equals(movieJsonString)) {
            return null;
        }

        JSONObject moviesJson = new JSONObject(movieJsonString);
        JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);
        Movie[] movies = new Movie[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject object = resultsArray.getJSONObject(i);
            movies[i] = new Movie(object.getString(ORIGINAL_TITLE),
                    object.getString(POSTER_PATH),
                    object.getString(OVERVIEW),
                    object.getString(VOTE_AVERAGE),
                    object.getString(RELEASE_DATE),
                    object.getString(BACKDROP_IMAGE_PATH));
        }

        ArrayList<Movie> movieArray = new ArrayList<>(Arrays.asList(movies));

        return movieArray;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);
        callbackDelegate.callBackAfterTaskFinished(movies);
    }
}
