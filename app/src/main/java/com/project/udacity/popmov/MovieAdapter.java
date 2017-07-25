package com.project.udacity.popmov;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ikhwan on 7/8/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final Context context;
    private final List<Movie> movies;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {

        Movie movie = getItem(position);
        // build URL
        String url = new StringBuilder()
                .append(context.getString(R.string.movie_poster_base_url))
                .append(context.getString(R.string.movie_poster_size))
                .append(movie.getPosterLocation().trim())
                .toString();

        // load with Picasso
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(holder.moviePoster);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_movie_grid) ImageView moviePoster;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
