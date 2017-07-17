package com.project.udacity.popmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        TextView movieTitle = (TextView) findViewById(R.id.tv_original_title);
        TextView movieReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        TextView movieRating = (TextView) findViewById(R.id.tv_rating);
        TextView movieOverview = (TextView) findViewById(R.id.tv_overview);
        ImageView moviePoster = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.data_for_detail_activity));

        movieTitle.setText(movie.getTitle());

        SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat resultDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String dateRelease = movie.getReleaseDate();

        try {
            Date releaseDate = jsonDateFormat.parse(dateRelease);
            dateRelease = resultDateFormat.format(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        movieReleaseDate.setText(dateRelease);
        movieRating.setText(movie.getRating());
        movieOverview.setText(movie.getSynopsis());

        String url = new StringBuilder()
                .append(this.getString(R.string.movie_poster_base_url))
                .append(this.getString(R.string.movie_poster_size))
                .append(movie.getPosterLocation().trim())
                .toString();

        // load with Picasso
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(moviePoster);
    }

}
