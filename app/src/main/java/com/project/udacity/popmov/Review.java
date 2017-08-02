package com.project.udacity.popmov;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ikhwan on 8/2/17.
 */

public class Review implements Parcelable {

    private final String reviewContent;
    private final String reviewUrl;
    private final String reviewAuthor;

    public Review(String content, String url, String author) {
        this.reviewContent = content;
        this.reviewUrl = url;
        this.reviewAuthor = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.reviewContent);
        dest.writeString(this.reviewUrl);
        dest.writeString(this.reviewAuthor);
    }

    private Review(Parcel in) {
        this.reviewContent = in.readString();
        this.reviewUrl = in.readString();
        this.reviewAuthor = in.readString();
    }


    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
