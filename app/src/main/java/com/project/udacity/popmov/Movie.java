package com.project.udacity.popmov;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ikhwan on 7/8/17.
 */

public class Movie implements Parcelable {

    private final String id;
    private final String title;
    private final String posterLocation;
    private final String synopsis;
    private final String rating;
    private final String releaseDate;
    private final String backdropImageLocation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterLocation);
        dest.writeString(this.synopsis);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.backdropImageLocation);
    }

    public Movie(String id, String title, String posterLocation, String synopsis, String rating, String releaseDate , String backdropImageLocation) {
        this.id = id;
        this.title = title;
        this.posterLocation = posterLocation;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.backdropImageLocation = backdropImageLocation;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.posterLocation = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readString();
        this.releaseDate = in.readString();
        this.backdropImageLocation = in.readString();
    }

    // getter


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterLocation() {
        return posterLocation;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropImageLocation() {
        return backdropImageLocation;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
