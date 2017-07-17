package com.project.udacity.popmov;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ikhwan on 7/8/17.
 */

public class MovieAdapter extends BaseAdapter {

    private final Context context;
    private final List<Movie> movies;

    MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        View grid;
        ImageView moviePoster;

        if (convertView == null) {
            grid = new View(context);
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.movie_item, parent, false);
        } else {
            grid = (View) convertView;
        }

        // build URL
        String url = new StringBuilder()
                .append(context.getString(R.string.movie_poster_base_url))
                .append(context.getString(R.string.movie_poster_size))
                .append(movie.getPosterLocation().trim())
                .toString();

        moviePoster = (ImageView) grid.findViewById(R.id.item_movie_grid);

        // load with Picasso
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.image_not_found)
                .into(moviePoster);

        return grid;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }
}
