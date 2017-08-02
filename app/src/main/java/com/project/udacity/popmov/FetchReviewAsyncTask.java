package com.project.udacity.popmov;

import android.net.Uri;
import android.os.AsyncTask;

import org.jetbrains.annotations.Nullable;
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
 * Created by ikhwan on 8/2/17.
 */

public class FetchReviewAsyncTask  extends AsyncTask<String, Void, ArrayList<Review>> {

    private final ReviewTaskCallback callbackDelegate;

    public interface ReviewTaskCallback {
        void processReviewAfterTaskFinished(ArrayList<Review> reviewArrayList);
    }

    FetchReviewAsyncTask(ReviewTaskCallback reviewTaskCallback) {
        this.callbackDelegate = reviewTaskCallback;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String reviewJsonString = null;
        String BASE_URL = "https://api.themoviedb.org/3/movie";
        String API_KEY_PARAM = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(params[0])
                .appendPath("reviews")
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

            reviewJsonString = builder.toString();

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
            return getReviewsFromJson(reviewJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    private ArrayList<Review> getReviewsFromJson(String reviewJsonString) throws JSONException {
        final String RESULTS = "results";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String REVIEW_URL = "url";

        if (reviewJsonString == null || "".equals(reviewJsonString)) {
            return null;
        }

        JSONObject trailersJson = new JSONObject(reviewJsonString);
        JSONArray resultsArray = trailersJson.getJSONArray(RESULTS);
        Review[] reviews = new Review[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject object = resultsArray.getJSONObject(i);
            reviews[i] = new Review(object.getString(REVIEW_CONTENT),
                    object.getString(REVIEW_URL),
                    object.getString(REVIEW_AUTHOR));
        }

        ArrayList<Review> reviewArrayList = new ArrayList<>(Arrays.asList(reviews));

        return reviewArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        super.onPostExecute(reviews);
        callbackDelegate.processReviewAfterTaskFinished(reviews);
    }
}
