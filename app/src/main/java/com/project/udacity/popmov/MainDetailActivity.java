package com.project.udacity.popmov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_original_title) TextView movieTitle;
    @BindView(R.id.tv_release_date) TextView movieReleaseDate;
    @BindView(R.id.tv_rating) TextView movieRating;
    @BindView(R.id.tv_overview) TextView movieOverview;
    @BindView(R.id.iv_movie_poster) ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.data_for_detail_activity));

        movieTitle.setText(movie.getTitle());
        setTitle(movie.getTitle());

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
