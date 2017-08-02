package com.project.udacity.popmov;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ikhwan on 7/27/17.
 */

public class Trailer implements Parcelable {

    private final String trailerId;
    private final String trailerKey;

    public Trailer(String id, String key) {
        this.trailerId = id;
        this.trailerKey = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trailerId);
        dest.writeString(this.trailerKey);
    }

    private Trailer(Parcel in) {
        this.trailerId = in.readString();
        this.trailerKey = in.readString();
    }

    public String getTrailerId() {
        return trailerId;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
