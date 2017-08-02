package com.project.udacity.popmov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ikhwan on 7/27/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.TrailerViewHolder> {

    private final Context context;
    private final ArrayList<Trailer> trailers;
    private LayoutInflater layoutInflater;

    public MovieTrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MovieTrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_trailers, parent, false);
        MovieTrailerAdapter.TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        String trailerKey = trailer.getTrailerKey();
        String thumbnailTrailerURL = context.getString(R.string.movie_thumb_trailer_url)
                .concat(trailerKey)
                .concat(context.getString(R.string.movie_thumb_trailer_name));
        Picasso.with(context).load(thumbnailTrailerURL)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailer_image)
        ImageView imageView;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
