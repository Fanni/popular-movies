package com.project.udacity.popmov;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

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
 * Created by ikhwan on 7/29/17.
 */

public class FetchTrailerAsyncTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final TrailerTaskCallback callbackDelegate;

    public interface TrailerTaskCallback {
        void callBackAfterTaskFinished(ArrayList<Trailer> trailerArrayList);
    }

    FetchTrailerAsyncTask(TrailerTaskCallback trailerTaskCallback) {
        this.callbackDelegate = trailerTaskCallback;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String trailerJsonString = null;
        String BASE_URL = "https://api.themoviedb.org/3/movie";
        String API_KEY_PARAM = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(params[0])
                .appendPath("videos")
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

            trailerJsonString = builder.toString();

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
            return getTrailersFromJson(trailerJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    private ArrayList<Trailer> getTrailersFromJson(String trailerJsonString) throws JSONException {
        final String RESULTS = "results";
        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";

        if (trailerJsonString == null || "".equals(trailerJsonString)) {
            return null;
        }

        JSONObject trailersJson = new JSONObject(trailerJsonString);
        JSONArray resultsArray = trailersJson.getJSONArray(RESULTS);
        Trailer[] trailers = new Trailer[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject object = resultsArray.getJSONObject(i);
            trailers[i] = new Trailer(object.getString(TRAILER_ID),
                    object.getString(TRAILER_KEY));
        }

        ArrayList<Trailer> trailerArrayList = new ArrayList<>(Arrays.asList(trailers));

        return trailerArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        super.onPostExecute(trailers);
        callbackDelegate.callBackAfterTaskFinished(trailers);
    }
}
