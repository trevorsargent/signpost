package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by student on 3/17/16.
 */
public class Post {

    @SerializedName("id")
    int mId;

    @SerializedName("title")
    String mTitle;

    @SerializedName("lat")
    double mLat;

    @SerializedName("long")
    double mLng;

    public Post(int mId, String mTitle, double mLat, double mLng) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public double getLat() {
        return mLat;
    }

    public double getLng() {
        return mLng;
    }

    public String getTitle() {
        return mTitle;
    }
}
