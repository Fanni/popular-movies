package com.project.udacity.popmov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ikhwan on 8/2/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewHolder> {

    Context context;
    ArrayList<Review> reviews = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ReviewClickListener reviewClickListener;

    public MovieReviewAdapter(Context context, ArrayList<Review> data) {
        this.context = context;
        this.reviews = data;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public MovieReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_review, parent, false);
        MovieReviewAdapter.ReviewHolder viewHolder = new ReviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieReviewAdapter.ReviewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.reviewText.setText(review.getReviewContent());
        holder.authorText.setText(review.getReviewAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public Review getReview(int position) {
        return reviews.get(position);
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.review_author) TextView authorText;
        @BindView(R.id.review) TextView reviewText;


        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (reviewClickListener != null) {
                reviewClickListener.onReviewClick(v, getAdapterPosition());
            }
        }
    }

    public void setReviewClickListener(ReviewClickListener reviewClickListener) {
        this.reviewClickListener = reviewClickListener;
    }

    public interface ReviewClickListener {
        void onReviewClick(View view, int position);
    }
}
