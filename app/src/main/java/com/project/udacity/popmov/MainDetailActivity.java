package com.project.udacity.popmov;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class MainDetailActivity extends AppCompatActivity implements FetchTrailerAsyncTask.TrailerTaskCallback,
        FetchReviewAsyncTask.ReviewTaskCallback,
        MovieTrailerAdapter.TrailerClickListener,
        MovieReviewAdapter.ReviewClickListener {

    @BindView(R.id.tv_original_title) TextView movieTitle;
    @BindView(R.id.tv_release_date) TextView movieReleaseDate;
    @BindView(R.id.tv_rating) TextView movieRating;
    @BindView(R.id.tv_overview) TextView movieOverview;
    @BindView(R.id.iv_movie_poster) ImageView moviePoster;
    @BindView(R.id.iv_collapsing_image) ImageView backdropPoster;
    @BindView(R.id.trailers_recyclerView) RecyclerView recyclerViewMovieTrailers;
    @BindView(R.id.review_recycleview) RecyclerView recyclerViewMovieReviews;

    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieReviewAdapter movieReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.data_for_detail_activity));

        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovieTrailers.setLayoutManager(trailerLayoutManager);

        FetchTrailerAsyncTask fetchTrailerAsyncTask = new FetchTrailerAsyncTask(MainDetailActivity.this);
        fetchTrailerAsyncTask.execute(movie.getId());

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMovieReviews.setLayoutManager(reviewLayoutManager);

        FetchReviewAsyncTask fetchReviewAsyncTask = new FetchReviewAsyncTask(MainDetailActivity.this);
        fetchReviewAsyncTask.execute(movie.getId());

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

        String backdropUrl = new StringBuilder()
                .append(this.getString(R.string.movie_poster_base_url))
                .append(this.getString(R.string.movie_backdrop_size))
                .append(movie.getBackdropImageLocation().trim())
                .toString();

        // load with Picasso
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(moviePoster);

        Picasso.with(this)
                .load(backdropUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(backdropPoster);

    }

    @Override
    public void callBackAfterTaskFinished(ArrayList<Trailer> trailerArrayList) {
        if (trailerArrayList.size() == 0) {
            trailerArrayList = new ArrayList<>();
        }
        movieTrailerAdapter = new MovieTrailerAdapter(this, trailerArrayList);
        movieTrailerAdapter.setTrailerClickListener(this);
        recyclerViewMovieTrailers.setAdapter(movieTrailerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Trailer trailer = movieTrailerAdapter.getTrailer(position);
        String url = "https://www.youtube.com/watch?v=".concat(trailer.getTrailerKey());
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void processReviewAfterTaskFinished(ArrayList<Review> reviewArrayList) {
        if (reviewArrayList.size() == 0) {
            reviewArrayList = new ArrayList<>();
        }
        movieReviewAdapter = new MovieReviewAdapter(this, reviewArrayList);
        movieReviewAdapter.setReviewClickListener(this);
        recyclerViewMovieReviews.setAdapter(movieReviewAdapter);
    }

    @Override
    public void onReviewClick(View view, int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Review review = movieReviewAdapter.getReview(position);
        intent.setData(Uri.parse(review.getReviewUrl()));
        startActivity(intent);
    }
}
