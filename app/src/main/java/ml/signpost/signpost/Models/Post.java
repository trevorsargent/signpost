package ml.signpost.signpost.Models;

/**
 * Created by student on 3/17/16.
 */
public class Post {
    int mId;
    String mTitle;
    double mLat;
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
